package org.nuist.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.nuist.po.StudentExamAnswerPO;

import java.util.List;

/**
 * 学生考试答案数据访问接口
 */
@Mapper
public interface StudentExamMapper extends BaseMapper<StudentExamAnswerPO> {
    @Select("SELECT DISTINCT student_id FROM student_exam_answer WHERE exam_id=#{examId}")
    List<Long> getStudentIdsByExamAnswer(Long examId);
} 