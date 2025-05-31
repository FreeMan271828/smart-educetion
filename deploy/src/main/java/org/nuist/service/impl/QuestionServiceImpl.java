package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.nuist.bo.QuestionBO;
import org.nuist.mapper.QuestionMapper;
import org.nuist.po.QuestionPo;
import org.nuist.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, QuestionPo> implements QuestionService {

    private final QuestionMapper questionMapper;

    @Override
    public QuestionBO getQuestionById(Long id) {
        if (id == null) {
            return null;
        }
        QuestionPo questionPo = questionMapper.selectById(id);
        if (questionPo == null) {
            return null;
        }
        return QuestionBO.fromQuestion(questionPo);
    }

    @Override
    public List<QuestionBO> getQuestionsByTeacherId(Long teacherId) {
        if (teacherId == null) {
            return new ArrayList<>();
        }
        return convertToQuestionBO(
                list(Wrappers.<QuestionPo>lambdaQuery()
                        .eq(QuestionPo::getTeacherId, teacherId)));
    }

    @Override
    public List<QuestionBO> getQuestionsByType(String questionType) {
        if (questionType == null || questionType.isEmpty()) {
            return new ArrayList<>();
        }
        return convertToQuestionBO(
                list(Wrappers.<QuestionPo>lambdaQuery()
                        .eq(QuestionPo::getQuestionType, questionType)));
    }

    @Override
    public List<QuestionBO> getQuestionsByDifficulty(String difficulty) {
        if (difficulty == null || difficulty.isEmpty()) {
            return new ArrayList<>();
        }
        return convertToQuestionBO(
                list(Wrappers.<QuestionPo>lambdaQuery()
                        .eq(QuestionPo::getDifficulty, difficulty)));
    }

    @Override
    public List<QuestionBO> getQuestionsByKnowledgeId(Long knowledgeId) {
        if (knowledgeId == null) {
            return new ArrayList<>();
        }
        return convertToQuestionBO(
                list(Wrappers.<QuestionPo>lambdaQuery()
                        .eq(QuestionPo::getKnowledgeId, knowledgeId)));
    }

    @Override
    public List<QuestionBO> getQuestionsByConditionInKnowledge(
            Long knowledgeId,
            String questionType,
            String difficulty,
            LocalDate startTime,
            LocalDate endTime
    ) {
        if (knowledgeId == null) {
            return new ArrayList<>();
        }
        // 仅包含一个知识点中的问题
        LambdaQueryWrapper<QuestionPo> wrapper = Wrappers.<QuestionPo>lambdaQuery().eq(QuestionPo::getKnowledgeId, knowledgeId);

        // 筛选问题类型
        if (StringUtils.hasText(questionType)) {
            wrapper.eq(QuestionPo::getQuestionType, questionType);
        }
        // 筛选问题难度
        if (StringUtils.hasText(difficulty)) {
            wrapper.eq(QuestionPo::getDifficulty, difficulty);
        }
        // 限定创建时间起始
        if (startTime != null) {
            wrapper.ge(QuestionPo::getCreatedAt, startTime.atStartOfDay());
        }
        // 限定创建时间结束
        if (endTime != null) {
            wrapper.le(QuestionPo::getCreatedAt, endTime.atTime(LocalTime.MAX));
        }

        return convertToQuestionBO(list(wrapper));
    }

    @Override
    public QuestionBO saveQuestion(QuestionBO questionBO) {
        QuestionPo persistedQuestionPo = questionBO.toQuestion();
        
        // 设置创建和更新时间
        LocalDateTime now = LocalDateTime.now();
        persistedQuestionPo.setCreatedAt(now);
        persistedQuestionPo.setUpdatedAt(now);
        
        questionMapper.insert(persistedQuestionPo);
        return QuestionBO.fromQuestion(persistedQuestionPo);
    }

    @Override
    public QuestionBO updateQuestion(QuestionBO questionBO) {
        if (questionBO == null || questionBO.getQuestionId() == null) {
            return null;
        }
        
        QuestionPo questionPo = questionBO.toQuestion();
        questionPo.setUpdatedAt(LocalDateTime.now());
        
        questionMapper.updateById(questionPo);
        return QuestionBO.fromQuestion(questionPo);
    }

    @Override
    public boolean deleteQuestion(Long id) {
        if (id == null) {
            return false;
        }
        return questionMapper.deleteById(id) > 0;
    }
    
    /**
     * 将PO列表转换为BO列表
     * @param poList PO列表
     * @return BO列表
     */
    private List<QuestionBO> convertToQuestionBO(List<QuestionPo> poList) {
        if (poList == null || poList.isEmpty()) {
            return new ArrayList<>();
        }
        return poList.stream()
                .map(QuestionBO::fromQuestion)
                .collect(Collectors.toList());
    }
}