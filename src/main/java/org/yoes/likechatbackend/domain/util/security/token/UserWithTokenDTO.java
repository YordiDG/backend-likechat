package org.yoes.likechatbackend.domain.util.security.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.yoes.likechatbackend.domain.model.entities.Impl.UserDetailsImpl;
import org.yoes.likechatbackend.domain.model.entities.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserWithTokenDTO {
    @Value("${secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private User user;
    private String token;

    public UserWithTokenDTO(User user, String token) {
        this.user = user;
        this.token = token;
    }








    public UserWithTokenDTO() {
    }

    public String generateToken(String secretKey, long expirationTime) {
        UserDetails userDetails = new UserDetailsImpl(user);
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }


    // Getters y setters
}
