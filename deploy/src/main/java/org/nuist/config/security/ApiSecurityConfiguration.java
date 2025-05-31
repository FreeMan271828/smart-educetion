package org.nuist.config.security;

import lombok.RequiredArgsConstructor;
import org.nuist.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class ApiSecurityConfiguration {

    private final JwtFilter jwtFilter;

    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http.with(new CommonSecurityConfigurer(), Customizer.withDefaults())
                .securityMatcher("/api/**")
                .authorizeHttpRequests(req -> req
                        .requestMatchers(
                                "/api/student/register",
                                "/api/teacher/register"
                        ).permitAll()
                        .anyRequest().authenticated()
                ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
