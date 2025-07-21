package com.tbread.book.authentication;

import com.tbread.book.user.entity.User;
import com.tbread.book.user.entity.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private final String username;
    private final String password;
    @Getter
    private final UserRole userRole;
    @Getter
    private final String ci;

    public UserDetailsImpl(final User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.userRole = user.getUserRole();
        this.ci = user.getCi();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.getRole()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
