package org.nuist.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.nuist.mapper.RoleMapper;
import org.nuist.mapper.UserMapper;
import org.nuist.po.Role;
import org.nuist.po.User;
import org.nuist.po.UserRole;
import org.nuist.mapper.UserRoleMapper;
import org.nuist.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MockDataConfiguration implements CommandLineRunner {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN_USERNAME = "admin";
    private static final String NORMAL_USERNAME = "user";
    public static final String STUDENT_USERNAME = "student1";
    public static final String TEACHER_USERNAME = "teacher1";

    @Override
    @Transactional
    public void run(String... args) {
        log.info("开始初始化模拟数据...");

        // 初始化角色
        initRoles();

        // 初始化管理员用户
        initAdminUser();

        // 初始化学生用户
        initStudent();

        // 初始化教师用户
        initTeacher();

        log.info("模拟数据初始化完成");
    }

    private void initRoles() {
        // 检查并创建默认角色
        for (RoleEnum roleEnum : RoleEnum.values()) {
            String roleName = "ROLE_" + roleEnum.name();
            QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role_name", roleName);

            Role existingRole = roleMapper.selectOne(queryWrapper);

            if (existingRole == null) {
                Role role = new Role();
                role.setRoleName(roleName);
                roleMapper.insert(role);
                log.info("创建角色: {}", roleName);
            } else {
                log.info("角色已存在: {}", roleName);
            }
        }
    }

    private void initAdminUser() {
        // 检查管理员用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", ADMIN_USERNAME);

        User existingUser = userMapper.selectOne(queryWrapper);

        if (existingUser == null) {
            // 创建管理员用户
            User adminUser = new User();
            adminUser.setUsername(ADMIN_USERNAME);
            adminUser.setPassword(passwordEncoder.encode("123456"));
            userMapper.insert(adminUser);

            // 为管理员分配所有角色
            assignRolesToAdmin(adminUser.getId());

            log.info("创建管理员用户: {}", ADMIN_USERNAME);
        } else {
            log.info("管理员用户已存在: {}", ADMIN_USERNAME);
        }
    }

    private void assignRolesToAdmin(Long userId) {
        // 获取所有角色
        List<Role> roles = roleMapper.selectList(null);

        // 分配所有角色给管理员
        for (Role role : roles) {
            // 检查是否已分配角色
            QueryWrapper<UserRole> userRoleQuery = new QueryWrapper<>();
            userRoleQuery.eq("user_id", userId);
            userRoleQuery.eq("role_id", role.getId());

            if (userRoleMapper.selectCount(userRoleQuery) == 0) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(role.getId());
                userRoleMapper.insert(userRole);
                log.info("为管理员分配角色: {}", role.getRoleName());
            }
        }
    }

    private void initStudent() {
        // 检查普通用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", STUDENT_USERNAME);

        User existingUser = userMapper.selectOne(queryWrapper);

        if (existingUser == null) {
            // 创建普通用户
            User normalUser = new User();
            normalUser.setUsername(STUDENT_USERNAME);
            normalUser.setPassword(passwordEncoder.encode("123456"));
            userMapper.insert(normalUser);

            // 分配ROLE_USER角色
            QueryWrapper<Role> roleQuery = new QueryWrapper<>();
            roleQuery.eq("role_name", "ROLE_" + RoleEnum.STUDENT.name());
            Role userRole = roleMapper.selectOne(roleQuery);
            if (userRole != null) {
                UserRole userRoleRel = new UserRole();
                userRoleRel.setUserId(normalUser.getId());
                userRoleRel.setRoleId(userRole.getId());
                userRoleMapper.insert(userRoleRel);
                log.info("为学生用户分配角色: {}", userRole.getRoleName());
            } else {
                log.warn("未找到ROLE_STUDENT角色，无法为学生用户分配角色");
            }
            log.info("创建学生用户: {}", STUDENT_USERNAME);
        } else {
            log.info("学生用户已存在: {}", STUDENT_USERNAME);
        }
    }

    private void initTeacher() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", TEACHER_USERNAME);

        User existingUser = userMapper.selectOne(queryWrapper);

        if (existingUser == null) {
            User normalUser = new User();
            normalUser.setUsername(TEACHER_USERNAME);
            normalUser.setPassword(passwordEncoder.encode("123456"));
            userMapper.insert(normalUser);

            // 分配ROLE_TEACHER角色
            QueryWrapper<Role> roleQuery = new QueryWrapper<>();
            roleQuery.eq("role_name", "ROLE_" + RoleEnum.TEACHER.name());
            Role userRole = roleMapper.selectOne(roleQuery);
            if (userRole != null) {
                UserRole userRoleRel = new UserRole();
                userRoleRel.setUserId(normalUser.getId());
                userRoleRel.setRoleId(userRole.getId());
                userRoleMapper.insert(userRoleRel);
                log.info("为教师用户分配角色: {}", userRole.getRoleName());
            } else {
                log.warn("未找到ROLE_TEACHER角色，无法为教师分配角色");
            }
            log.info("创建教师用户: {}", TEACHER_USERNAME);
        } else {
            log.info("普通教师已存在: {}", TEACHER_USERNAME);
        }
    }
}