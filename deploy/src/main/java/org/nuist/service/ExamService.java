package org.nuist.service;

import org.nuist.bo.ExamBO;

import java.util.List;

public interface ExamService {
    /**
     * 根据考试ID查找一个对象
     * @param id 考试ID
     * @return 考试对象
     */
    ExamBO getExamById(Long id);

    /**
     * 查找一个课程下的全部考试
     * @param courseId 课程ID
     * @return 考试列表
     */
    List<ExamBO> getExamsByCourseId(Long courseId);

    /**
     * 查找一个教师负责的全部考试
     * @param teacherId 教师ID
     * @return 考试列表
     */
    List<ExamBO> getExamsByTeacherId(Long teacherId);

    /**
     * 查询一个课程中，由某个教师负责的考试
     * @param courseId 目标课程ID
     * @param teacherId 目标教师ID
     * @return 考试列表
     */
    List<ExamBO> getExamsByTeacherInCourse(Long courseId, Long teacherId);

    /**
     * 在课程中按照类型（考试/练习题）来查找exam
     * @param courseId 课程ID
     * @param type 类型
     * @return 考试列表
     */
    List<ExamBO> getExamsInCourseByType(Long courseId, String type);

    /**
     * 持久化一个考试实体
     * @param examBo dto
     * @return 持久化后的业务对象（附带主键和时间字段）
     */
    ExamBO saveExam(ExamBO examBo);

    /**
     * 更新一个考试
     * @param examBo dto，提供必要的更新内容
     * @return 更新后的业务对象
     */
    ExamBO updateExam(ExamBO examBo);

    /**
     * 删除一个指定考试
     * @param id 考试ID
     * @return 是否正确删除
     */
    boolean deleteExam(Long id);
}
