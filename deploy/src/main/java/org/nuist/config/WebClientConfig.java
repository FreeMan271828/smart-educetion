package org.nuist.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

// WebClientConfig.java
@Configuration
public class WebClientConfig {

    @Value("${rag.service.url}")
    private String ragServiceUrl;

    @Bean
    public WebClient webClient() {
        // 配置连接池和超时设置
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMinutes(5));

        return WebClient.builder()
                .baseUrl(ragServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
