package org.nuist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nuist.po.ProblemBankPO;

import java.util.List;
import java.util.Map;

public interface ProblemBankMapper extends BaseMapper<ProblemBankPO> {

    List<ProblemBankPO> selectPage(Map<String, Object> params);
    long countPage(Map<String, Object> params);
}

