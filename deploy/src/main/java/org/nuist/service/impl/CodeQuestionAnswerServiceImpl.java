package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.nuist.bo.CodeQuestionAnswerBO;
import org.nuist.dto.response.JudgeResultDTO;
import org.nuist.mapper.CodeQuestionAnswerMapper;
import org.nuist.mapper.CodeQuestionMapper;
import org.nuist.po.CodeQuestionAnswerPO;
import org.nuist.po.CodeQuestionPO;
import org.nuist.service.CodeQuestionAnswerService;
import org.nuist.service.JudgeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CodeQuestionAnswerServiceImpl implements CodeQuestionAnswerService {

    private final CodeQuestionAnswerMapper mapper;
    private final JudgeService judgeService;
    private final CodeQuestionMapper codeQuestionMapper;

    @Override
    public CodeQuestionAnswerBO getCQuestionAnswer(Long id) {
        if (id == null) {
            return null;
        }
        CodeQuestionAnswerPO answer = mapper.selectById(id);
        return CodeQuestionAnswerBO.fromPO(answer);
    }

    @Override
    public List<CodeQuestionAnswerBO> getAnswersInCQuestion(Long codeQuestionId) {
        if (codeQuestionId == null) {
            return new ArrayList<>();
        }
        return convertToList(mapper.selectList(
                Wrappers.<CodeQuestionAnswerPO>lambdaQuery()
                        .eq(CodeQuestionAnswerPO::getCodeQuestionId, codeQuestionId)
        ));
    }

    @Override
    public List<CodeQuestionAnswerBO> getAnswersByStudent(Long studentId) {
        if (studentId == null) {
            return new ArrayList<>();
        }
        return convertToList(mapper.selectList(
                Wrappers.<CodeQuestionAnswerPO>lambdaQuery()
                        .eq(CodeQuestionAnswerPO::getStudentId, studentId)
        ));
    }

    @Override
    public List<CodeQuestionAnswerBO> getStudentAnswersInCQuestion(Long codeQuestionId, Long studentId) {
        if (codeQuestionId == null || studentId == null) {
            return new ArrayList<>();
        }
        return convertToList(mapper.selectList(
                Wrappers.<CodeQuestionAnswerPO>lambdaQuery()
                        .eq(CodeQuestionAnswerPO::getCodeQuestionId, codeQuestionId)
                        .eq(CodeQuestionAnswerPO::getStudentId, studentId)
        ));
    }

    @Override
    public boolean isAccepted(Long studentId, Long codeQuestionId) {
        if (studentId == null || codeQuestionId == null) {
            return false;
        }
        return mapper.selectCount(
                Wrappers.<CodeQuestionAnswerPO>lambdaQuery()
                        .eq(CodeQuestionAnswerPO::getStudentId, studentId)
                        .eq(CodeQuestionAnswerPO::getCodeQuestionId, codeQuestionId)
                        .eq(CodeQuestionAnswerPO::getStatus, "ACCEPTED")
        ) > 0;
    }

    @Override
    public CodeQuestionAnswerBO submit(Long studentId, Long codeQuestionId, String studentCode, String language) {
        if (studentId == null || codeQuestionId == null || !StringUtils.hasText(language) || StringUtils.hasText(studentCode)) {
            return null;
        }
        // 获取对应编程题实体
        CodeQuestionPO codeQuestionPO = codeQuestionMapper.selectById(codeQuestionId);
        if (codeQuestionPO == null) {
            return null;
        }

        CodeQuestionAnswerPO po = new CodeQuestionAnswerPO();
        po.setCodeQuestionId(codeQuestionId);
        po.setStudentId(studentId);
        po.setLanguage(language);
        po.setStudentCode(studentCode);

        // 评测
        JudgeResultDTO result = judgeService.judge(
                po.getLanguage(),
                po.getStudentCode(),
                codeQuestionPO.getCaseInputs(),
                codeQuestionPO.getCaseOutputs()
        );
        po.setStatus(result.getStatus());
        po.setCaseAccepted((int) result.getStatusPerCase().stream().filter("ACCEPTED"::equals).count());
        po.setCaseTotal(codeQuestionPO.getCaseOutputs().size());
        po.setTimeMs(result.getTimeMs());

        mapper.insert(po);
        return CodeQuestionAnswerBO.fromPO(po);
    }

    private List<CodeQuestionAnswerBO> convertToList(List<CodeQuestionAnswerPO> poList) {
        return poList.stream().map(CodeQuestionAnswerBO::fromPO).collect(Collectors.toList());
    }
}
