package org.nuist.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@Configuration
public class WebSecurityCustomizerConfiguration {

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        // 全局放行Swagger文档和error等URL
//        return web -> web
//                .ignoring()
//                .requestMatchers(
//                        "/v3/api-docs/**",
//                        "/api-docs/**",
//                        "/swagger-resources",
//                        "/swagger-resources/**",
//                        "/configuration/ui",
//                        "/configuration/security",
//                        "/swagger-ui",
//                        "/swagger-ui/**",
//                        "/webjars/**",
//                        "/swagger-ui.html",
//                        "/doc.html",
//                        "/error"
//                );
//    }

}
