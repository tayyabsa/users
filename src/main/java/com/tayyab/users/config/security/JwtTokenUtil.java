package com.tayyab.users.config.security;

import com.tayyab.users.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenUtil {

    @Autowired
    AppProperties appProperties;

    public String generateToken(String userId,String userName,String tokenSecret) {
       return Jwts.builder()
                .setId(userId)
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + 1728000000l))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    public String validateToken(String token,String tokenSecret) {
        return Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws( token )
                .getBody()
                .getSubject();
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(appProperties.getTokenSecret())
                .parseClaimsJws( token )
                .getBody();
    }
}
