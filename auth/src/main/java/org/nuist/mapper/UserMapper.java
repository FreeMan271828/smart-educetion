package org.nuist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nuist.model.User;
import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User> {
    // 查询单个用户及其角色
    User selectUserWithRoles(String username);

    // 批量查询用户及其角色
    List<User> selectAllUsersWithRoles();

    // 根据条件查询用户及其角色
    List<User> selectUsersWithRolesByCondition(Map<String, Object> params);
}
