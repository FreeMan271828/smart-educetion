package org.nuist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.nuist.po.AssignmentPO;

import java.util.List;

public interface AssignmentMapper extends BaseMapper<AssignmentPO> {
    /**
     * 自定义查询：按课程ID和类型查询作业
     * 使用类型转换解决枚举类型问题
     *
     * @param courseId 课程ID
     * @param type 作业类型
     * @return 作业列表
     */
    @Select("SELECT * FROM assignment WHERE course_id = #{courseId} AND type = #{type}::assignment_type")
    List<AssignmentPO> findByCourseIdAndType(
            @Param("courseId") Long courseId,
            @Param("type") String type
    );

    /**
     * 自定义查询：按创建者ID查询作业
     *
     * @param creatorId 创建者ID
     * @return 作业列表
     */
    @Select("SELECT * FROM assignment WHERE creator_id = #{creatorId}")
    List<AssignmentPO> findByCreatorId(@Param("creatorId") Long creatorId);

    /**
     * 自定义查询：按创建者ID和类型查询作业
     * 使用类型转换解决枚举类型问题
     *
     * @param creatorId 创建者ID
     * @param type 作业类型
     * @return 作业列表
     */
    @Select("SELECT * FROM assignment " +
            "WHERE creator_id = #{creatorId} " +
            "AND type = #{type}::assignment_type")
    List<AssignmentPO> findByCreatorIdAndType(
            @Param("creatorId") Long creatorId,
            @Param("type") String type

    );
}
