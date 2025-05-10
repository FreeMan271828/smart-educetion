package org.nuist.dao;

import org.nuist.persist_object.StudentPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 学生数据访问接口
 */
@Mapper
public interface StudentMapper extends BaseMapper<StudentPO> {

} 