package org.nuist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.nuist.po.CourseSelectionPO;

import java.util.List;

/**
 *  选课
 */
@Mapper
public interface CourseSelectionMapper extends BaseMapper<CourseSelectionPO> {

}
