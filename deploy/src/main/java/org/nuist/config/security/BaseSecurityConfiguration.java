package org.nuist.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class BaseSecurityConfiguration {

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    public SecurityFilterChain baseFilterChain(HttpSecurity http) throws Exception {
        http
                .with(new CommonSecurityConfigurer(), Customizer.withDefaults())
                .authorizeHttpRequests(req -> req
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html",
                                "/doc.html",
                                "/error"
                        ).permitAll()
                        .anyRequest().permitAll());
        return http.build();
    }

}
