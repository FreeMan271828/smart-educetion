package org.nuist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.nuist.po.KnowledgeUnitPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface KnowledgeUnitMapper extends BaseMapper<KnowledgeUnitPO> {
    /**
     * 自定义分页查询
     * @param params 查询参数
     * @return 分页数据列表
     */
    List<KnowledgeUnitPO> selectBySubjectAndStatusPage(Map<String, Object> params);

    /**
     * 获取总记录数
     * @param params 查询参数
     * @return 总记录数
     */
    long countBySubjectAndStatus(Map<String, Object> params);


    /**
     * 按状态分页查询
     * @param paramMap 包含分页参数和状态
     */
    List<KnowledgeUnitPO> selectByStatusPage(Map<String, Object> paramMap);

    /**
     * 按状态计数
     * @param paramMap 包含状态参数
     */
    Long countByStatus(Map<String, Object> paramMap);
}
