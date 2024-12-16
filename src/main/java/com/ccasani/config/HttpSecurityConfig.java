package com.ccasani.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class HttpSecurityConfig {

    private final AuthenticationProvider daoAuthProvider;
    private final CustomAuthorizationFilter customAuthorizationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        SecurityFilterChain filterChain = http
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessMagConfig -> sessMagConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAuthProvider)
                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authReqConfig -> {
                    authReqConfig.requestMatchers(HttpMethod.POST, "/api/usuarios/registro").permitAll();
                    authReqConfig.requestMatchers(HttpMethod.POST, "/api/usuarios/login").permitAll();
                    authReqConfig.anyRequest().authenticated();
                })
                .exceptionHandling(ex -> {
                    ex.accessDeniedHandler(this.accessDeniedHandler);
                    ex.authenticationEntryPoint(this.authenticationEntryPoint);
                })
                .build();

        return filterChain;
    }
}
