package org.nuist.service;

import org.nuist.bo.TeacherBO;
import org.nuist.dto.AddTeacherDTO;
import org.nuist.dto.UpdateTeacherDTO;
import org.nuist.entity.TokenResponse;

public interface TeacherService {

    /**
     * 根据教师ID查询实体
     * @param id 教师ID
     * @return 教师业务对象
     */
    TeacherBO getTeacherById(Long id);

    /**
     * 根据唯一username查询实体
     * @param username 教师唯一username
     * @return 教师业务对象
     */
    TeacherBO getTeacherByUsername(String username);

    /**
     * 注册教师用户
     * @param addTeacherDTO dto
     * @return 登录Token
     */
    TokenResponse saveTeacher(AddTeacherDTO addTeacherDTO);

    TeacherBO updateTeacher(UpdateTeacherDTO updateTeacherDTO);

    /**
     * @param id 目标教师ID
     * @param username 新用户名
     * @return 修改是否成功
     */
    boolean changeTeacherUsername(Long id, String username);

}
