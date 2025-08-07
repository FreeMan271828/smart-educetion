package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.nuist.bo.CodeQuestionBO;
import org.nuist.mapper.CodeQuestionMapper;
import org.nuist.po.CodeQuestionPO;
import org.nuist.service.CodeQuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CodeQuestionServiceImpl implements CodeQuestionService {

    private final CodeQuestionMapper codeQuestionMapper;

    @Override
    public CodeQuestionBO insertCodeQuestion(CodeQuestionBO codeQuestion) {
        if (codeQuestion == null) {
            return null;
        }
        codeQuestion.setId(null);
        CodeQuestionPO po = codeQuestion.toPO();

        if (!validateTestCase(po.getCaseInputs(), po.getCaseOutputs())) {
            throw new IllegalArgumentException("用例输入和用例输出的数量不合法");
        }

        codeQuestionMapper.insert(po);
        codeQuestion.setId(po.getId());

        return codeQuestion;
    }

    @Override
    public CodeQuestionBO getCodeQuestionById(Long codeQuestionId) {
        CodeQuestionPO po = codeQuestionMapper.selectById(codeQuestionId);
        return CodeQuestionBO.fromPO(po);
    }

    @Override
    public List<CodeQuestionBO> getCQuestionsInExam(Long examId) {
        return convertCodeQuestionList(
                codeQuestionMapper.selectList(
                        Wrappers.<CodeQuestionPO>lambdaQuery()
                                .eq(CodeQuestionPO::getExamId, examId)
                )
        );
    }

    @Override
    public CodeQuestionBO updateCodeQuestion(CodeQuestionBO codeQuestion) {
        if (codeQuestion == null || codeQuestion.getId() == null) {
            return null;
        }
        CodeQuestionPO po = codeQuestionMapper.selectById(codeQuestion.getId());
        if (po == null) {
            return null;
        }
        if (StringUtils.hasText(codeQuestion.getTitle())) {
            po.setTitle(codeQuestion.getTitle());
        }
        if (StringUtils.hasText(codeQuestion.getDescription())) {
            po.setDescription(codeQuestion.getDescription());
        }
        if (codeQuestion.getScorePoints() != null) {
            po.setScorePoints(codeQuestion.getScorePoints());
        }
        if (codeQuestion.getSampleInputs() != null) {
            po.setSampleInputs(codeQuestion.getSampleInputs());
        }
        if (codeQuestion.getSampleOutputs() != null) {
            po.setSampleOutputs(codeQuestion.getSampleOutputs());
        }
        if (codeQuestion.getCaseInputs() != null) {
            po.setCaseInputs(codeQuestion.getCaseInputs());
        }
        if (codeQuestion.getCaseOutputs() != null) {
            po.setCaseOutputs(codeQuestion.getCaseOutputs());
        }
        if (StringUtils.hasText(codeQuestion.getReferenceAnswer())) {
            po.setReferenceAnswer(codeQuestion.getReferenceAnswer());
        }

        if (!validateTestCase(po.getCaseInputs(), po.getCaseOutputs())) {
            throw new IllegalArgumentException("用例输入和用例输出的数量不合法");
        }

        po.setUpdatedAt(LocalDateTime.now());
        codeQuestionMapper.updateById(po);
        return CodeQuestionBO.fromPO(po);
    }

    @Override
    public boolean deleteCodeQuestionById(Long codeQuestionId) {
        return codeQuestionMapper.deleteById(codeQuestionId) > 0;
    }

    private boolean validateTestCase(List<String> inputs, List<String> outputs) {
        // 检验合法性：用例输入和用例输出的个数必须相等，除非用例输入为空（即不需要任何输入，这时仅允许1个用例输出）
        boolean cond1 = inputs.size() == outputs.size();
        boolean cond2 = inputs.isEmpty() && outputs.size() == 1;
        return cond1 || cond2;
    }

    private List<CodeQuestionBO> convertCodeQuestionList(List<CodeQuestionPO> codeQuestionPOList) {
        return codeQuestionPOList.stream().map(CodeQuestionBO::fromPO).collect(Collectors.toList());
    }
}
