package com.tbread.book.authentication.jwt;

import com.tbread.book.authentication.UserDetailsImpl;
import com.tbread.book.authentication.UserDetailsServiceImpl;
import com.tbread.book.authentication.entity.RefreshToken;
import com.tbread.book.authentication.repository.RefreshTokenRepository;
import com.tbread.book.common.ExpiringHashMap;
import com.tbread.book.common.TokenPackage;
import com.tbread.book.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtProcessor {

    @Getter
    public enum TimeUnit {
        SECOND(1000L),
        MINUTE(60 * 1000L),
        HOUR(60 * 60 * 1000L),
        DAY(24 * 60 * 60 * 1000L),
        WEEK(7 * 24 * 60 * 60 * 1000L);
        private final long value;

        private static final Map<String, TimeUnit> TimeUnitMatcher = Map.of(
                "second", TimeUnit.SECOND,
                "minute", TimeUnit.MINUTE,
                "hour", TimeUnit.HOUR,
                "day", TimeUnit.DAY,
                "week", TimeUnit.WEEK
        );

        TimeUnit(long value) {
            this.value = value;
        }

        public static long getTimeValue(String s) {
            return TimeUnitMatcher.get(s).getValue();
        }

    }

    public enum JwtType {
        ACCESS("Access-Token"), REFRESH("Refresh-Token");
        private String cookieName;

        JwtType(String cookieName) {
            this.cookieName = cookieName;
        }

        public String getCookieName() {
            return this.cookieName;
        }
    }

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailsServiceImpl userDetailsService;

    private final long ACCESS_TOKEN_VALID_TIME;
    private final long REFRESH_TOKEN_VALID_TIME;
    private final static ExpiringHashMap<String, Boolean> INVALIDATED_REFRESH_TOKEN = new ExpiringHashMap<>();

    private final String secretKey;

    @Autowired
    public JwtProcessor(@Value("${jwt.validate.time.access:#{null}}") String accessValidTime,
                        @Value("${jwt.validate.time.access.unit:#{null}}") String accessValidTimeUnit,
                        @Value("${jwt.validate.time.refresh:#{null}}") String refreshValidTime,
                        @Value("${jwt.validate.time.refresh.unit:#{null}}") String refreshValidTimeUnit,
                        @Value("${jwt.signing.secret:#{null}}") String rawSecretKey,
                        RefreshTokenRepository refreshTokenRepository,
                        UserDetailsServiceImpl userDetailsService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userDetailsService = userDetailsService;

        try {
            int parsedAccessValidTime = Integer.parseInt(accessValidTime);
            this.ACCESS_TOKEN_VALID_TIME = TimeUnit.getTimeValue(accessValidTimeUnit.toLowerCase()) * parsedAccessValidTime;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("올바르지않은 액세스토큰 유효시간입니다. (" + accessValidTime + ") jwt.validate.time.access 값을 확인해주세요.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("올바르지않은 액세스토큰 시간단위입니다. (" + accessValidTimeUnit + ") jwt.validate.time.access.unit 값을 확인해주세요. " + "유효값: [second,minute,hour,day,week]");
        }

        try {
            int parsedRefreshValidTime = Integer.parseInt(refreshValidTime);
            this.REFRESH_TOKEN_VALID_TIME = TimeUnit.getTimeValue(refreshValidTimeUnit.toLowerCase()) * parsedRefreshValidTime;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("올바르지않은 리프레시토큰 유효시간입니다. (" + refreshValidTime + ") jwt.validate.time.refresh 값을 확인해주세요.");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("올바르지않은 리프레시토큰 시간단위입니다. (" + refreshValidTimeUnit + ") jwt.validate.time.refresh.unit 값을 확인해주세요. " + "유효값: [second,minute,hour,day,week]");
        }

        try {
            this.secretKey = Base64.getEncoder().encodeToString(rawSecretKey.getBytes());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("올바르지않은 JWT 시크릿키입니다. (" + rawSecretKey + ") jwt.signing.secret 값을 확인해주세요.");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("JWT 시크릿키가 없습니다. jwt.signing.secret 값을 작성해주세요.");
        }

    }


    public String createToken(String username, JwtType type) {
        Date now = new Date();
        Date expiredAt = new Date(type.equals(JwtType.ACCESS) ? now.getTime() + ACCESS_TOKEN_VALID_TIME : now.getTime() + REFRESH_TOKEN_VALID_TIME);
        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .subject(username)
                .claims(Map.of("type", type))
                .issuedAt(now)
                .expiration(expiredAt)
                .compact();
        if (type.equals(JwtType.REFRESH)) {
            saveRefreshToken(userDetailsService.loadUserByUsername(username).getUser(), token, expiredAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        return token;
    }


    private void saveRefreshToken(User user, String token, LocalDateTime expiredAt) {
        RefreshToken refreshToken = new RefreshToken(user, token, expiredAt);
        refreshTokenRepository.save(refreshToken);
    }

    public Date getExpiration(String token) {
        return getClaims(token).getPayload().getExpiration();
    }

    protected Jws<Claims> getClaims(String token) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parseSignedClaims(token);
    }

    public TokenPackage extractToken(HttpServletRequest httpReq) {
        return new TokenPackage(httpReq);
    }

    public void invalidateRefreshToken(String token) {
        INVALIDATED_REFRESH_TOKEN.put(token, true, getExpiration(token));
    }

    public boolean isValidate(String token) {
        try {
            return !getClaims(token).getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isInvalidatedToken(TokenPackage tokenPackage) {
        return INVALIDATED_REFRESH_TOKEN.contains(tokenPackage.getRefreshToken());
    }

    public Cookie clearAccessCookie() {
        Cookie accessCookie = new Cookie(JwtType.ACCESS.getCookieName(), null);
        accessCookie.setMaxAge(0);
        accessCookie.setPath("/");
        accessCookie.setHttpOnly(true);
        return accessCookie;
    }

    public Cookie clearRefreshCookie() {
        Cookie refreshCookie = new Cookie(JwtType.REFRESH.getCookieName(), null);
        refreshCookie.setMaxAge(0);
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        return refreshCookie;
    }

    public UserDetailsImpl extractUserDetails(String token) {
        String username = getClaims(token).getPayload().getSubject();
        return userDetailsService.loadUserByUsername(username);
    }

    public Cookie setJwtCookie(String token, JwtType type) {
        Cookie tokenCookie = new Cookie(type.getCookieName(), token);
        tokenCookie.setPath("/");
        tokenCookie.setHttpOnly(true);
        tokenCookie.setMaxAge(Math.toIntExact(type.equals(JwtType.ACCESS) ? ACCESS_TOKEN_VALID_TIME / 1000 : REFRESH_TOKEN_VALID_TIME / 1000));
        return tokenCookie;
    }

    public Authentication getAuthentication(String token) {
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(extractUserDetails(token).getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}