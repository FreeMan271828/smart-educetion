package org.nuist.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nuist.po.StudentPO;

/**
 * 学生数据访问接口
 */
@Mapper
public interface StudentMapper extends BaseMapper<StudentPO> {

} 