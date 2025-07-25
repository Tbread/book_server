package com.tbread.book.config;

import com.tbread.book.authentication.RestfulLoginAuthenticationFilter;
import com.tbread.book.authentication.jwt.JwtFilterChain;
import com.tbread.book.authentication.jwt.JwtProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProcessor jwtProcessor;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        RestfulLoginAuthenticationFilter restfulLoginAuthenticationFilter = new RestfulLoginAuthenticationFilter(authenticationManager,jwtProcessor);
        http.sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        http.authorizeHttpRequests(request ->
                                request
                                        .requestMatchers("/h2-console").permitAll()
                                        .requestMatchers("/h2-console/**").permitAll()
                                        .requestMatchers("**").permitAll()
                )
                .addFilterAt(restfulLoginAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtFilterChain(jwtProcessor), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
