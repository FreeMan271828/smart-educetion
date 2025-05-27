package org.nuist.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nuist.po.StudentExamAnswerPO;

/**
 * 学生考试答案数据访问接口
 */
@Mapper
public interface StudentExamMapper extends BaseMapper<StudentExamAnswerPO> {

} 