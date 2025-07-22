package org.nuist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.nuist.dto.request.AdminChangePasswordDto;
import org.nuist.dto.request.ChangePasswordDto;
import org.nuist.dto.request.LoginRequestDto;
import org.nuist.dto.request.RefreshTokenDto;
import org.nuist.dto.response.LoginResponseDto;
import org.nuist.entity.TokenResponse;
import org.nuist.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nuist.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Tag(name = "auth", description = "用户鉴权API")
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    /**
     * 用户登录
     * @param dto 包含username和password的请求体
     * @return accessToken和refreshToken
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Validated LoginRequestDto dto) {
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

    /**
     * 修改用户密码
     * @param dto 请求体，包含用户名、旧密码和新密码
     * @return 修改密码后，重新签发的JWT token
     */
    @PutMapping("/change-password")
    public ResponseEntity<TokenResponse> changePassword(@RequestBody ChangePasswordDto dto) {
        return ResponseEntity.ok(userService.changePassword(dto.getUsername(), dto.getOldPassword(), dto.getNewPassword()));
    }

    @PutMapping("/admin-change-password")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "管理员：无验证修改用户密码")
    public ResponseEntity<Map<String, Object>> adminChangePassword(@RequestBody AdminChangePasswordDto dto) {
        boolean success = userService.adminChangePassword(dto.getUsername(), dto.getNewPassword());
        return ResponseEntity.ok(new HashMap<>() {{
            put("success", success);
            put("message", success ? "用户密码已更改" : "更改失败，请检查用户名合法性");
        }});
    }

    /**
     * 查看用户名是否可用
     * @param username 用户名
     * @return true：用户名可用；false：用户名已存在
     */
    @GetMapping("/check-available-username")
    public ResponseEntity<Boolean> checkAvailableUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.checkUsername(username));
    }

}
