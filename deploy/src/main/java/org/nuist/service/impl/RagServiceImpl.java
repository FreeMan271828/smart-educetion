package org.nuist.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class RagServiceImpl {

    private final WebClient webClient;

    @Autowired
    public RagServiceImpl(WebClient webClient) {
        this.webClient = webClient; // 正确注入WebClient
    }



    public Mono<Map<String, Object>> askQuestion(String question) {
        ParameterizedTypeReference<Map<String, Object>> typeRef =
                new ParameterizedTypeReference<>() {};
        return webClient.post()
                .uri("/chat")
                .bodyValue(createRequestBody(question))
                .retrieve()
                .bodyToMono(typeRef);
    }

    private Map<String, Object> createRequestBody(String question) {
        return Collections.singletonMap(
                "messages",
                List.of(
                        Map.of(
                                "role", "user",
                                "content", question
                        )
                )
        );
    }
}