package org.nuist.service;

import org.nuist.dto.response.JudgeResultDTO;

import java.util.List;

public interface JudgeService {
    /**
     * @param lang            编程语言（java | cpp | python）
     * @param code            目标代码
     * @param inputs          测试输入
     * @param expectedOutputs 预期输出
     * @return 评测结果
     */
    JudgeResultDTO judge(String lang, String code, List<String> inputs, List<String> expectedOutputs);
}
