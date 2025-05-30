package org.nuist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.nuist.po.Knowledge;

import java.util.List;

public interface KnowledgeMapper extends BaseMapper<Knowledge> {
    @Select("SELECT k.* FROM knowledge k " +
            "JOIN course_knowledge ck ON k.knowledge_id = ck.knowledge_id " +
            "WHERE ck.course_id = #{courseId} " +
            "ORDER BY ck.sequence_number")
    List<Knowledge> selectKnowledgeInCourseOrdered(Long courseId);
}
