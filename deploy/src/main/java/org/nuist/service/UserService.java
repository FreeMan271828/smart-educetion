package org.nuist.service;

import org.nuist.entity.TokenResponse;
import org.nuist.enums.RoleEnum;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    /**
     * 注册新用户持久化保存
     * @param username 唯一用户名
     * @param password 明文密码
     * @param role 用户角色
     * @return 认证JWT token
     */
    TokenResponse register(String username, String password, RoleEnum role);

    /**
     * 修改密码
     * @param username 目标用户名
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return JWT token
     */
    TokenResponse changePassword(String username, String oldPassword, String newPassword);

    /**
     * 检查用户名是否可用
     * @param username 目标用户名
     * @return true - 用户名可用；false - 用户名不可用
     */
    boolean checkUsername(String username);
}
