package org.nuist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.nuist.po.ProblemPO;

import java.util.List;

public interface ProblemMapper extends BaseMapper<ProblemPO> {

    /**
     * 按作业ID和题目类型查询题目
     * @param assignmentId 作业ID
     * @param type 题目类型
     * @return 题目列表
     */
    @Select("SELECT * FROM problem " +
            "WHERE assignment_id = #{assignmentId} " +
            "AND type = #{type}::public.problem_type " +
            "ORDER BY sequence ASC")
    List<ProblemPO> findByAssignmentIdAndType(
            @Param("assignmentId") Long assignmentId,
            @Param("type") String type
    );

    /**
     * 按题目类型查询题目
     * @param type 题目类型
     * @return 题目列表
     */
    @Select("SELECT * FROM problem " +
            "WHERE type = #{type}::public.problem_type")
    List<ProblemPO> findByType(@Param("type") String type);
}