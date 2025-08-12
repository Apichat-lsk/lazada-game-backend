package com.example.lazada_game.web.jwtverify;

import com.example.lazada_game.domain.model.Users;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret_key;

    @Value("${jwt.expiration}")
    private long expiration; // เปลี่ยนเป็น long (milliseconds)

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 1. สร้าง token
    public String generateToken(Users users) {
        return Jwts.builder()
                .setSubject(users.getUsername())
                .claim("uid",users.getId())
                .claim("tel",users.getTel())
                .claim("email",users.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 2. ดึง username จาก token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 3. ตรวจสอบว่า token หมดอายุหรือไม่
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 4. ตรวจสอบว่า token ใช้ได้หรือไม่
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 5. ดึงวันหมดอายุจาก token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 6. Utility: ดึง Claim ใด ๆ จาก token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
