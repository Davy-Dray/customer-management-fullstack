package com.ragnar.customer_management.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class JWTUtil {

    private static final String SECRET_KEY = "foobar_1234567_foobar_1234567-foobar_1234567";


    public String issueToken(String subject) {

        return issueToken(subject,Map.of());
    }
    public String issueToken(String subject,String ...scope) {

        return issueToken(subject,Map.of("scopes",scope));
    }


    public String issueToken(String subject, Map<String,Object> claims){

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer("https://dave.com")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(15, ChronoUnit.DAYS)))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private Key getSigninKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
