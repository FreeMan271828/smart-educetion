package org.nuist.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.nuist.persist_object.AttendancePO;

/**
 * 考勤数据访问接口
 */
@Mapper
public interface AttendanceMapper extends BaseMapper<AttendancePO> {
    
} 