package org.nuist.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.nuist.po.StudentExamAnswerPO;

import java.util.List;
import java.util.Map;

/**
 * 学生考试答案数据访问接口
 */
@Mapper
public interface StudentExamMapper extends BaseMapper<StudentExamAnswerPO> {
    @Select("SELECT DISTINCT student_id FROM student_exam_answer WHERE exam_id=#{examId}")
    List<Long> getStudentIdsByExamAnswer(Long examId);

    @Select("SELECT se.exam_id, e.title AS exam_title, SUM(se.score) AS total_score, COUNT(1) as question_count, MAX(se.updated_at) as exam_time " +
            "FROM student_exam_answer se " +
            "JOIN exam e ON e.exam_id=se.exam_id " +
            "WHERE se.student_id = #{studentId} " +
            "GROUP BY se.exam_id, e.title")
    List<Map<String, Object>> getStudentExamScores(Long studentId);

    @Select("SELECT se.exam_id, e.title AS exam_title, SUM(se.score) AS total_score, COUNT(1) as question_count, MAX(se.updated_at) as exam_time " +
            "FROM student_exam_answer se " +
            "JOIN exam e ON e.exam_id=se.exam_id " +
            "WHERE se.student_id = #{studentId} AND e.title ILIKE CONCAT('%', #{titleKeyword}, '%') " +
            "GROUP BY se.exam_id, e.title")
    List<Map<String, Object>> searchStudentExamScores(Long studentId, String titleKeyword);
} 