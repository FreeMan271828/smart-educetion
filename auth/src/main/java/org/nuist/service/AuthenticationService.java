package org.nuist.service;

import org.nuist.entity.TokenResponse;

public interface AuthenticationService {
    /**
     * 认证用户登录信息
     * @param username 用户登录名
     * @param password 明文密码
     * @return 认证JWT token
     */
    TokenResponse login(String username, String password);

    /**
     * 刷新登录状态，重新签发JWT token
     * @param refreshToken 上次登录时附带的refresh token
     * @return 新的认证JWT token
     */
    TokenResponse refresh(String refreshToken);
}
