package com.tayyab.users.config.filters;

import com.tayyab.users.config.AppProperties;
import com.tayyab.users.config.security.JwtTokenUtil;
import com.tayyab.users.config.security.UsersPrinciple;
import com.tayyab.users.entity.UsersEntity;
import com.tayyab.users.repository.UsersRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String JWT_TOKEN_PREFIX = "Bearer";

    private final UsersRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AppProperties appProperties;

    public AuthorizationFilter(AuthenticationManager authManager, UsersRepository userRepository, JwtTokenUtil jwtTokenUtil, AppProperties appProperties) {
        super(authManager);
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.appProperties = appProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader(AUTHORIZATION_HEADER);

        if (header == null || !header.startsWith(JWT_TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);

        if (token != null) {

            token = token.replace(JWT_TOKEN_PREFIX, "");
            String userName = jwtTokenUtil.validateToken(token,appProperties.getTokenSecret());

            if (userName != null) {
                Optional<UsersEntity> userEntity = userRepository.findByUserName(userName);
                if(!userEntity.isPresent()) return null;

                UsersPrinciple userPrincipal = new UsersPrinciple(userEntity.get());
                return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
            }
        }
        return null;
    }

}
