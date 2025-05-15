package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.nuist.business_object.TeacherBO;
import org.nuist.dto.AddTeacherDTO;
import org.nuist.dto.UpdateTeacherDTO;
import org.nuist.entity.TokenResponse;
import org.nuist.enums.RoleEnum;
import org.nuist.mapper.TeacherMapper;
import org.nuist.model.TeacherPO;
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
        // TODO 可以等Student和Teacher与Auth模块的User集成之后，再实现这里的正确逻辑
        return null;
    }
}
