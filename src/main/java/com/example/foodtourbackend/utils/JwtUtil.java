package com.example.foodtourbackend.utils;

import com.example.foodtourbackend.entity.Customer;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final SecretKey jwtSecretKey;
    private final int jwtExpirationMs;
    private final int jwtRefreshExpirationMs;

    public JwtUtil(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expirationMs}") int jwtExpirationMs,
            @Value("${jwt.refreshExpirationMs}") int jwtRefreshExpirationMs
    ) {
        // Tạo key từ chuỗi secret (đảm bảo chuỗi secret đủ dài cho HS512)
        this.jwtSecretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationMs = jwtExpirationMs;
        this.jwtRefreshExpirationMs = jwtRefreshExpirationMs;
    }

    // Sinh Access Token
    public String generateToken(Customer customer) {
        return Jwts.builder()
                .setSubject(customer.getEmail())
                .claim("fullName", customer.getFullName())
                .claim("id", customer.getId())
                .claim("role", customer.getRole())
                .claim("phoneNumber", customer.getPhoneNumber())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // Sinh Refresh Token
    public String generateRefreshToken(Customer customer) {
        return Jwts.builder()
                .setSubject(customer.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // Kiểm tra token có hết hạn không
    private boolean isTokenExpired(String token) {
        final Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
    // Lấy ID từ token
    public Long getUserIdFromToken(String token) {
        return extractClaim(token, claims -> claims.get("id", Long.class));
    }


    // Lấy email từ token
    public String getUserEmailFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Lấy tất cả thông tin user
    public Map<String, Object> getAllClaimsFromToken(String token) {
        return extractAllClaims(token);
    }

    // Kiểm tra token có hợp lệ và khớp với thông tin từ userDetails
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String email = getUserEmailFromToken(token);
            return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token đã hết hạn.");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Token không đúng định dạng.");
        } catch (Exception e) {
            throw new RuntimeException("Token không hợp lệ.");
        }
    }

    // Trích xuất thông tin từ token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Trích xuất tất cả claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
}
