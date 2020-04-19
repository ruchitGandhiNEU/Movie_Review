/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinema.TokenResponse;

import com.cinema.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Component;

/**
 *
 * @author Ruchit Gandhi <Ruchit at Northeasten.com>
 */
@Component
public class JWTTokenUtility implements Serializable {
    
    //Deletable
    //private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 24;
    
    private String secretKey = "MOVIEsecretKey";

    // get userId from jwt token
    public String getUserIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // get expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    //for retrieveing any information from token we will need the secretKey key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
    
    //check if the token has expired
    private boolean isTokenExpired(String token) {
        final Date exp = getExpirationDateFromToken(token);
        return exp.before(new Date());
    }
    
    //generate token for user
    public String generateToken(User u) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, u.getId());
    }

    private String doGenerateToken(Map<String, Object> claims, int subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(subject))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }
    
    //validate token
    public boolean validateToken(String token) {
        final String userId = getUserIdFromToken(token);
        return (!isTokenExpired(token));
    }
    
}
