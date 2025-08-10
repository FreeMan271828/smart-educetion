package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nuist.bo.ProblemBankBO;
import org.nuist.constant.ProblemBankOriginConstant;
import org.nuist.dto.ProblemBankDTO;
import org.nuist.mapper.ProblemBankMapper;
import org.nuist.po.ProblemBankPO;
import org.nuist.service.ProblemBankService;
import org.nuist.util.PageQueryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProblemBankServiceImpl extends ServiceImpl<ProblemBankMapper, ProblemBankPO> implements ProblemBankService {
    @Autowired
    private ProblemBankMapper problemBankMapper;

    @Override
    public ProblemBankPO getById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Map<String, Object> saveProblemBank(ProblemBankDTO problemBankDTO) {
        ProblemBankPO problemBankPo = problemBankDTO.toProblemBankPO();
        problemBankPo.setOrigin(ProblemBankOriginConstant.PRESET);
        if(baseMapper.insert(problemBankPo)>0){
            return Map.of("插入完成",problemBankDTO.getTitle());
        }
        else{
            return Map.of("插入失败",problemBankDTO.getTitle());
        }
    }

    @Override
    public Map<String, Object> updateProblemBank(ProblemBankDTO problemBankDTO, Long id) {
        ProblemBankPO problemBankPo = problemBankDTO.toProblemBankPO();
        problemBankPo.setOrigin(ProblemBankOriginConstant.PRESET);
        problemBankPo.setId(id);
        if(baseMapper.updateById(problemBankPo)>0){
            return Map.of("更新完成",problemBankDTO.getTitle());
        }
        else{
            return Map.of("更新失败",problemBankDTO.getTitle());
        }
    }

    @Override
    public Map<String, Object> deleteProblemBank(Long id) {

        if(baseMapper.deleteById(id)>0){
            return Map.of("删除完成",id);
        }
        else{
            return Map.of("删除失败",id);
        }
    }

    @Override
    public List<ProblemBankBO> searchProblemBank(String keyword) {

        LambdaQueryWrapper<ProblemBankPO> wrapper = Wrappers.lambdaQuery();
        wrapper.and(q ->
                q.like(ProblemBankPO::getTitle,keyword)
                        .or()
                        .like(ProblemBankPO::getContent,keyword)
        );
        List<ProblemBankPO> poList = baseMapper.selectList(wrapper);
        return poList.stream()
                .map(ProblemBankBO::fromProblemBankPO)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> pageProblemBank(int pageNum, int pageSize) {
        // 1. 准备查询参数
        Map<String, Object> paramMap = new HashMap<>();

        // 2. 构建分页参数
        PageQueryHelper.buildPageQuery(paramMap, pageNum, pageSize);

        // 3. 执行分页查询
        List<ProblemBankPO> records = problemBankMapper.selectPage(paramMap);

        // 4. 获取总记录数
        long total = problemBankMapper.countPage(paramMap);

        // 5. 转换为BO列表
        List<ProblemBankBO> boList = records.stream()
                .map(ProblemBankBO::fromProblemBankPO)
                .collect(Collectors.toList());

        // 6. 返回分页结果
        return Map.of(
                "current", pageNum,
                "size", pageSize,
                "total", total,
                "pages", PageQueryHelper.calculatePages(total, pageSize),
                "data", boList
        );
    }

    @Override
    public Map<String, Object> batchSaveProblemBank(List<ProblemBankDTO> problemBankDTOList) {

        List<ProblemBankPO> problemBankPoList = problemBankDTOList.stream()
                .map(dto -> {
                    ProblemBankPO po = dto.toProblemBankPO(); // 转换为 PO
                    po.setOrigin("PRESET"); // 修改 origin 字段
                    return po;
                })
                .collect(Collectors.toList());

        int batchSize = 1000; // 推荐1000-2000条/批
        boolean success = saveBatch(problemBankPoList, batchSize); // 使用MyBatis-Plus原生方法[2,6](@ref)

        // 3. 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "批量插入成功" : "批量插入失败");
        result.put("data", problemBankPoList.size()); // 返回插入记录数
        return result;
    }


}
