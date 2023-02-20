package com.service;

import com.properties.JwtProperties;
import com.util.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.HashMap;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String findUsername(String token) {
        return exportToken(token, Claims::getSubject);
    }

    private <T> T exportToken(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = Jwts.parserBuilder()
            .setSigningKey(getSecretKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claimsTFunction.apply(claims);
    }

    public boolean checkToken(String jwt, UserDetails userDetails) {
        final String username = findUsername(jwt);
        return username.equals(userDetails.getUsername()) &&
               !exportToken(jwt, Claims::getExpiration).before(DateUtil.nowAsDate());
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .setClaims(new HashMap<>())
            .setSubject(userDetails.getUsername())
            .setIssuedAt(DateUtil.nowAsDate())
            .setExpiration(DateUtil.add(DateUtil.nowAsDate(), Duration.ofDays(7)))
            .signWith(getSecretKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    private Key getSecretKey() {
        byte[] key = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(key);
    }
}
