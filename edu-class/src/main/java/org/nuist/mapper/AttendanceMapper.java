package org.nuist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.nuist.po.AttendancePO;

/**
 * 考勤数据访问接口
 */
@Mapper
public interface AttendanceMapper extends BaseMapper<AttendancePO> {
    
} 