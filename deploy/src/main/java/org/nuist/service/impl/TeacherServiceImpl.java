package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.nuist.bo.TeacherBO;
import org.nuist.dto.AddTeacherDTO;
import org.nuist.dto.UpdateTeacherDTO;
import org.nuist.entity.TokenResponse;
import org.nuist.enums.RoleEnum;
import org.nuist.mapper.TeacherMapper;
import org.nuist.mapper.UserMapper;
import org.nuist.po.TeacherPO;
import org.nuist.po.User;
import org.nuist.service.TeacherService;
import org.nuist.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, TeacherPO> implements TeacherService {

    private final TeacherMapper teacherMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public TeacherBO getTeacherById(Long id) {
        if (id == null) {
            return null;
        }
        TeacherPO teacherPO = teacherMapper.selectById(id);
        if (teacherPO == null) {
            return null;
        }
        return TeacherBO.fromTeacherPO(teacherPO);
    }

    @Override
    public TeacherBO getTeacherByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        TeacherPO teacher = teacherMapper.selectOne(
                Wrappers.<TeacherPO>lambdaQuery().eq(TeacherPO::getUsername, username)
        );
        if (teacher == null) {
            return null;
        }
        return TeacherBO.fromTeacherPO(teacher);
    }

    @Override
    public TokenResponse saveTeacher(AddTeacherDTO addTeacherDTO) {
        TeacherPO teacher = new TeacherPO();
        teacher.setUsername(addTeacherDTO.getUsername());
//        teacher.setPassword(addTeacherDTO.getPassword());
        teacher.setEmail(addTeacherDTO.getEmail());
        teacher.setPhone(addTeacherDTO.getPhone());
        teacher.setFullName(addTeacherDTO.getFullName());

        teacherMapper.insert(teacher);
        return userService.register(addTeacherDTO.getUsername(), addTeacherDTO.getPassword(), RoleEnum.TEACHER);
    }

    @Override
    public TeacherBO updateTeacher(UpdateTeacherDTO updateTeacherDTO) {
        if (updateTeacherDTO == null || updateTeacherDTO.getTeacherId() == null) {
            return null;
        }
        TeacherPO teacher = teacherMapper.selectById(updateTeacherDTO.getTeacherId());
        if (teacher == null) {
            return null;
        }
        if (StringUtils.hasText(updateTeacherDTO.getFullName())) {
            teacher.setFullName(updateTeacherDTO.getFullName());
        }
        if (StringUtils.hasText(updateTeacherDTO.getPhone())) {
            teacher.setPhone(updateTeacherDTO.getPhone());
        }
        if (StringUtils.hasText(updateTeacherDTO.getEmail())) {
            teacher.setEmail(updateTeacherDTO.getEmail());
        }
        teacherMapper.updateById(teacher);
        return TeacherBO.fromTeacherPO(teacher);
    }

    @Override
    public boolean changeTeacherUsername(Long id, String username) {
        if (id == null) {
            return false;
        }
        TeacherPO teacher = teacherMapper.selectById(id);
        if (teacher == null) {
            return false;
        }
        // 检查可用用户名
        if (!userService.checkUsername(username)) {
            return false;
        }
        // 同时更新User. 如果Teacher能查到not null，那么user一定存在，无需再判断null
        User user = userMapper.selectOne(
                Wrappers.<User>lambdaQuery().eq(User::getUsername, teacher.getUsername())
        );
        teacher.setUsername(username);
        user.setUsername(username);
        try {
            teacherMapper.updateById(teacher);
            userMapper.updateById(user);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
