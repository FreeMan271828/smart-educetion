package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nuist.entity.TokenResponse;
import org.nuist.enums.RoleEnum;
import org.nuist.mapper.RoleMapper;
import org.nuist.mapper.UserMapper;
import org.nuist.mapper.UserRoleMapper;
import org.nuist.model.Role;
import org.nuist.model.User;
import org.nuist.model.UserRole;
import org.nuist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.nuist.util.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectUserWithRoles(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    @Override
    public TokenResponse register(String username, String password, RoleEnum role) {
        // 首先持久化user
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userMapper.insert(user);
        // 然后获取Role并设置关联
        Role targetRole = roleMapper.selectOne(
                Wrappers.<Role>lambdaQuery()
                        .eq(Role::getRoleName, "ROLE_" + role.name())
        );
        UserRole userRole = new UserRole();
        userRole.setRoleId(targetRole.getId());
        userRole.setUserId(user.getId());
        userRoleMapper.insert(userRole);
        // 最后生成token
        return jwtUtil.generateToken(username);
    }

    @Override
    public TokenResponse changePassword(String username, String oldPassword, String newPassword) {
        User user = userMapper.selectUserWithRoles(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("旧密码不正确");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
        return jwtUtil.generateToken(username);
    }

    @Override
    public boolean checkUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("username不可留空");
        }
        User user = userMapper.selectOne(
                Wrappers.<User>lambdaQuery()
                        .eq(User::getUsername, username)
        );
        return user == null;
    }

    public List<User> getAllUsersWithRoles() {
        return userMapper.selectAllUsersWithRoles();
    }

    public List<User> getUsersByCondition(String username, String roleName) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("roleName", roleName);
        return userMapper.selectUsersWithRolesByCondition(params);
    }
}
