package org.nuist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.nuist.po.StudentProblemAnswerPO;

import java.util.List;

public interface StudentProblemAnswerMapper extends BaseMapper<StudentProblemAnswerPO> {
    // 根据answerId查询单个作答记录的finalScore
    @Select("""
    SELECT COALESCE(manual_score, auto_score) AS final_score 
    FROM student_problem_answer 
    WHERE answer_id = #{answerId}
""")
    Double getFinalScoreByAnswerId(@Param("answerId") Long answerId);

    // 根据assignmentId查询该作业所有作答记录的finalScore
    @Select("""

    SELECT COALESCE(manual_score, auto_score) AS final_score 
    FROM student_problem_answer 
    WHERE assignment_id = #{assignmentId}
""")
    List<Double> getFinalScoresByAssignmentId(@Param("assignmentId") Long assignmentId);


    @Select("""
    SELECT COUNT(*) 
    FROM student_problem_answer
    WHERE 
        student_id = #{studentId}
        AND assignment_id = #{assignmentId}
        AND status = #{status}::data.answer_status  
""")
    int countSubmittedAnswers(
            @Param("studentId") Long studentId,
            @Param("assignmentId") Long assignmentId,
            @Param("status") String status  // 接收枚举名称（String）
    );
}
