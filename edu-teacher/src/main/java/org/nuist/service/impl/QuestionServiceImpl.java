package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.nuist.bo.QuestionBO;
import org.nuist.dto.AddQuestionDTO;
import org.nuist.dto.QuestionQueryDTO;
import org.nuist.dto.UpdateQuestionDTO;
import org.nuist.mapper.QuestionMapper;
import org.nuist.model.Question;
import org.nuist.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    private final QuestionMapper questionMapper;

    @Override
    public QuestionBO getQuestionByID(Long id) {
        if (id == null) {
            return null;
        }
        Question question = questionMapper.selectById(id);
        if (question == null) {
            return null;
        }
        return QuestionBO.fromQuestion(question);
    }

    @Override
    public List<QuestionBO> getQuestionsInKnowledge(Long knowledgeId) {
        if (knowledgeId == null) {
            return new ArrayList<>();
        }
        return convertToQuestionBO(
                list(Wrappers.<Question>lambdaQuery()
                        .eq(Question::getKnowledgeId, knowledgeId)));
    }

    @Override
    public List<QuestionBO> getQuestionsByTeacherId(Long teacherId) {
        if (teacherId == null) {
            return new ArrayList<>();
        }
        return convertToQuestionBO(
                list(Wrappers.<Question>lambdaQuery()
                        .eq(Question::getTeacherId, teacherId)));
    }

    @Override
    public List<QuestionBO> searchQuestionsByKeyword(Long knowledgeId, String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return new ArrayList<>();
        }
        return convertToQuestionBO(
                list(Wrappers.<Question>lambdaQuery()
                        .eq(Question::getKnowledgeId, knowledgeId)
                        .like(Question::getContent, keyword)));
    }

    @Override
    public List<QuestionBO> getQuestionsByConditionInKnowledge(Long knowledgeId, QuestionQueryDTO queryDTO) {
        if (queryDTO == null || knowledgeId == null) {
            return new ArrayList<>();
        }
        // 仅包含一个知识点中的问题
        LambdaQueryWrapper<Question> wrapper = Wrappers.<Question>lambdaQuery().eq(Question::getKnowledgeId, knowledgeId);

        // 筛选问题类型
        if (StringUtils.hasText(queryDTO.getQuestionType())) {
            wrapper.eq(Question::getQuestionType, queryDTO.getQuestionType());
        }
        // 筛选问题难度
        if (StringUtils.hasText(queryDTO.getDifficulty())) {
            wrapper.eq(Question::getDifficulty, queryDTO.getDifficulty());
        }
        // 限定创建时间起始
        if (queryDTO.getStartTime() != null) {
            wrapper.ge(Question::getCreatedAt, queryDTO.getStartTime().atStartOfDay());
        }
        // 限定创建时间结束
        if (queryDTO.getEndTime() != null) {
            wrapper.le(Question::getCreatedAt, queryDTO.getEndTime().atTime(LocalTime.MAX));
        }

        return convertToQuestionBO(list(wrapper));
    }

    @Override
    public QuestionBO saveQuestion(AddQuestionDTO addQuestionDTO) {
        Question question = new Question();
        question.setTeacherId(addQuestionDTO.getTeacherId());
        question.setContent(addQuestionDTO.getContent());
        question.setDifficulty(addQuestionDTO.getDifficulty());
        question.setQuestionType(addQuestionDTO.getQuestionType());
        question.setKnowledgeId(addQuestionDTO.getKnowledgeId());
        question.setReferenceAnswer(addQuestionDTO.getReferenceAnswer());
        question.setScorePoints(addQuestionDTO.getScorePoints());
        question.setAnswer(addQuestionDTO.getAnswer());

        questionMapper.insert(question);
        return QuestionBO.fromQuestion(question);
    }

    @Override
    public QuestionBO updateQuestion(UpdateQuestionDTO updateQuestionDTO) {
        Question question = questionMapper.selectById(updateQuestionDTO.getQuestionId());
        if (question == null) {
            throw new IllegalArgumentException("Question ID " + updateQuestionDTO.getQuestionId() + " not found");
        }
        if (StringUtils.hasText(updateQuestionDTO.getDifficulty())) {
            question.setDifficulty(updateQuestionDTO.getDifficulty());
        }
        if (StringUtils.hasText(updateQuestionDTO.getQuestionType())) {
            question.setQuestionType(updateQuestionDTO.getQuestionType());
        }
        if (StringUtils.hasText(updateQuestionDTO.getReferenceAnswer())) {
            question.setReferenceAnswer(updateQuestionDTO.getReferenceAnswer());
        }
        if (StringUtils.hasText(updateQuestionDTO.getAnswer())) {
            question.setAnswer(updateQuestionDTO.getAnswer());
        }
        if (StringUtils.hasText(updateQuestionDTO.getContent())) {
            question.setContent(updateQuestionDTO.getContent());
        }
        if (updateQuestionDTO.getScorePoints() != null) {
            question.setScorePoints(updateQuestionDTO.getScorePoints());
        }

        questionMapper.updateById(question);
        return QuestionBO.fromQuestion(question);
    }

    @Override
    public boolean deleteQuestion(Long id) {
        return questionMapper.deleteById(id) > 0;
    }

    private List<QuestionBO> convertToQuestionBO(List<Question> questions) {
        return questions.stream()
                .map(QuestionBO::fromQuestion)
                .collect(Collectors.toList());
    }
}
