package club.management.club.services;


import club.management.club.entities.Authority;
import club.management.club.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // Boom this good idea to use Function
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String generateToken(User validateUser) {
        return generateToken(new HashMap<>(), validateUser);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            User validateUser
    ) {
        return buildToken(extraClaims, validateUser, jwtExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            User validateUser,
            long expiration
    ) {
        var authorities = validateUser.getAuthorities()
                .stream().
                map(Authority::getName)
                .toList();
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(validateUser.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .claim("authorities", authorities)
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, User validateUser) {
        final String username = extractUsername(token);
        return (username.equals(validateUser.getEmail())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate=extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
