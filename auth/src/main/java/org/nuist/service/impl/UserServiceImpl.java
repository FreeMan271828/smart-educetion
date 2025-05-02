package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.nuist.mapper.UserMapper;
import org.nuist.model.User;
import org.nuist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectUserWithRoles(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
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
