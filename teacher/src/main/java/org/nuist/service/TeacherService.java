package org.nuist.service;

import org.nuist.business_object.TeacherBO;
import org.nuist.dto.AddTeacherDTO;
import org.nuist.dto.UpdateTeacherDTO;

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

    TeacherBO saveTeacher(AddTeacherDTO addTeacherDTO);

    TeacherBO updateTeacher(UpdateTeacherDTO updateTeacherDTO);

}
