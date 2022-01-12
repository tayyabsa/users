package com.tayyab.users.config.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tayyab.users.config.AppProperties;
import com.tayyab.users.config.security.JwtTokenUtil;
import com.tayyab.users.config.security.UsersPrinciple;
import com.tayyab.users.dto.LoginDto;
import com.tayyab.users.dto.UserRequestDto;
import com.tayyab.users.entity.UsersEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final AppProperties appProperties;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthenticationFilter(AuthenticationManager authenticationManager, AppProperties appProperties, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.appProperties = appProperties;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    /**
     * when user try to login it will call
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {

            LoginDto creds = new ObjectMapper()
                    .readValue(req.getInputStream(), LoginDto.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUserName(),
                            creds.getPassword(),
                            new ArrayList<>())
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Generate jwt token when user is successfully login and put in header
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        UsersEntity usersEntity = ((UsersPrinciple) auth.getPrincipal()).getUsersEntity();
        String token = jwtTokenUtil.generateToken(usersEntity.getUserId(),usersEntity.getUserName(), appProperties.getTokenSecret());
        res.addHeader("Authorization", "Bearer " + token);
    }

}
