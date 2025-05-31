package org.nuist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.nuist.po.CoursePO;

/**
 * 课程数据访问接口
 */
@Mapper
public interface CourseMapper extends BaseMapper<CoursePO> {
    // 使用MyBatis-Plus提供的方法，不需要自定义方法
    // selectOne + lambdaQueryWrapper可以根据名称查询课程
    // selectById可以根据ID查询课程
} 