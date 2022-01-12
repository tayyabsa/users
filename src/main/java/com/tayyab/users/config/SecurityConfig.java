package com.tayyab.users.config;

import com.tayyab.users.config.filters.AuthenticationFilter;
import com.tayyab.users.config.filters.AuthorizationFilter;
import com.tayyab.users.config.security.JwtTokenUtil;
import com.tayyab.users.repository.UsersRepository;
import com.tayyab.users.service.UsersService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UsersRepository usersRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AppProperties appProperties;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UsersService usersService;

    public SecurityConfig(UsersRepository usersRepository, JwtTokenUtil jwtTokenUtil, AppProperties appProperties, BCryptPasswordEncoder bCryptPasswordEncoder, UsersService usersService) {
        this.usersRepository = usersRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.appProperties = appProperties;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.usersService = usersService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable().authorizeRequests()
                .antMatchers( "/v1/users/signup").permitAll()
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()
                .antMatchers("/v1/users/login", "/v1/health").permitAll()
                .anyRequest().authenticated().and()
                .addFilter(getAuthenticationFilter())
                .addFilter(new AuthorizationFilter(authenticationManager(), usersRepository,jwtTokenUtil, appProperties))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersService).passwordEncoder(bCryptPasswordEncoder);
    }

    protected AuthenticationFilter getAuthenticationFilter() throws Exception {
        final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager(), appProperties, jwtTokenUtil);
        filter.setFilterProcessesUrl("/v1/users/login");
        return filter;
    }
}