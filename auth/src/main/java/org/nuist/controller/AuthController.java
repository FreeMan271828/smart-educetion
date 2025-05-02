package org.nuist.controller;

import org.nuist.dto.request.LoginRequestDto;
import org.nuist.entity.TokenResponse;
import org.nuist.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "auth", description = "用户鉴权API")
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Validated LoginRequestDto dto) {
        return ResponseEntity.ok(authenticationService.login(dto.getUsername(), dto.getPassword()));
    }

}
