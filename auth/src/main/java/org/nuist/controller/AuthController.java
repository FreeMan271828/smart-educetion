package org.nuist.controller;

import org.nuist.dto.request.LoginRequestDto;
import org.nuist.dto.request.RefreshTokenDto;
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

    /**
     * 用户登录
     * @param dto 包含username和password的请求体
     * @return accessToken和refreshToken
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Validated LoginRequestDto dto) {
        return ResponseEntity.ok(authenticationService.login(dto.getUsername(), dto.getPassword()));
    }

    /**
     * 提供refreshToken，刷新登录状态
     * @param dto 仅包含refreshToken的请求体
     * @return 刷新后的accessToken和refreshToken
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody @Validated RefreshTokenDto dto) {
        return ResponseEntity.ok(authenticationService.refresh(dto.getRefreshToken()));
    }

}
