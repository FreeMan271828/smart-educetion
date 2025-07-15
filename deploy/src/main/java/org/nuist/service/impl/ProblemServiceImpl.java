package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nuist.bo.ProblemBO;
import org.nuist.mapper.ProblemMapper;
import org.nuist.po.ProblemPO;
import org.nuist.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProblemServiceImpl implements ProblemService {

    @Autowired
    private ProblemMapper problemMapper;

    @Override
    public ProblemBO getProblemById(Long problemId) {
        if (problemId == null) {
            return null;
        }
        ProblemPO problemPo = problemMapper.selectById(problemId);
        if (problemPo == null) {
            return null;
        }
        return ProblemBO.fromProblem(problemPo);
    }

    @Override
    public List<ProblemBO> getProblemsByAssignmentId(Long assignmentId) {
        if (assignmentId == null) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<ProblemPO> query = Wrappers.lambdaQuery();
        query.eq(ProblemPO::getAssignmentId, assignmentId)
                .orderByAsc(ProblemPO::getSequence);

        List<ProblemPO> problems = problemMapper.selectList(query);
        return convertToProblemBO(problems);
    }

    @Override
    public List<ProblemBO> getProblemsByAssignmentIdAndType(Long assignmentId, String type) {
        if (assignmentId == null || !StringUtils.hasText(type)) {
            return Collections.emptyList();
        }

        // 使用自定义查询方法（解决枚举类型问题）
        List<ProblemPO> problems = problemMapper.findByAssignmentIdAndType(assignmentId, type);
        return convertToProblemBO(problems);
    }

    @Override
    public List<ProblemBO> getProblemsByType(String type) {
        if (!StringUtils.hasText(type)) {
            return Collections.emptyList();
        }

        // 使用自定义查询方法（解决枚举类型问题）
        List<ProblemPO> problems = problemMapper.findByType(type);
        return convertToProblemBO(problems);
    }

    @Override
    public ProblemBO saveProblem(ProblemBO problemBO) {
        // 转换为持久化对象
        ProblemPO problemPo = problemBO.toProblem();

        // 插入数据库
        int result = problemMapper.insert(problemPo);
        if (result <= 0) {
            throw new RuntimeException("保存题目失败");
        }

        // 返回带ID的业务对象
        return ProblemBO.fromProblem(problemPo);
    }

    @Override
    public ProblemBO updateProblem(ProblemBO problemBO) {
        // 检查ID是否存在
        if (problemBO.getProblemId() == null) {
            throw new IllegalArgumentException("题目ID不能为空");
        }

        // 获取现有题目
        ProblemPO existing = problemMapper.selectById(problemBO.getProblemId());
        if (existing == null) {
            throw new IllegalArgumentException("题目不存在: " + problemBO.getProblemId());
        }

        // 更新可修改字段
        if (problemBO.getTitle() != null) {
            existing.setTitle(problemBO.getTitle());
        }
        if (problemBO.getContent() != null) {
            existing.setContent(problemBO.getContent());
        }
        if (problemBO.getType() != null) {
            existing.setType(problemBO.getType());
        }
        if (problemBO.getAutoGrading() != null) {
            existing.setAutoGrading(problemBO.getAutoGrading());
        }
        if (problemBO.getExpectedAnswer() != null) {
            existing.setExpectedAnswer(problemBO.getExpectedAnswer());
        }
        if (problemBO.getScore() != null) {
            existing.setScore(problemBO.getScore());
        }
        if (problemBO.getSequence() != null) {
            existing.setSequence(problemBO.getSequence());
        }

        // 更新数据库
        int result = problemMapper.updateById(existing);
        if (result <= 0) {
            throw new RuntimeException("更新题目失败");
        }

        return ProblemBO.fromProblem(existing);
    }

    @Override
    public boolean deleteProblem(Long problemId) {
        if (problemId == null) {
            return false;
        }
        return problemMapper.deleteById(problemId) > 0;
    }

    /**
     * 将持久化对象列表转换为业务对象列表
     * @param problems 持久化对象列表
     * @return 业务对象列表
     */
    private List<ProblemBO> convertToProblemBO(List<ProblemPO> problems) {
        return problems.stream()
                .map(ProblemBO::fromProblem)
                .collect(Collectors.toList());
    }



}