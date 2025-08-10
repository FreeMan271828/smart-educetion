package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.nuist.bo.KnowledgeBO;
import org.nuist.bo.KnowledgeUnitBO;
import org.nuist.mapper.KnowledgeUnitMapper;
import org.nuist.po.KnowledgeUnitPO;
import org.nuist.service.KnowledgeUnitService;
import org.nuist.util.PageQueryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.extension.toolkit.Db.saveBatch;

@Service
public class KnowledgeUnitServiceImpl extends ServiceImpl<KnowledgeUnitMapper, KnowledgeUnitPO> implements KnowledgeUnitService {
    @Autowired
    private KnowledgeUnitMapper knowledgeUnitMapper;

    private final WebClient webClient ;

    public KnowledgeUnitServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }


    @Override
    public Map<String, Object> analyzeTargetKnowledge(String target, String subject) {
        // 1. 获取预置知识单元（status=0）
        List<KnowledgeUnitPO> presetList = this.lambdaQuery()
                .eq(KnowledgeUnitPO::getSubject, subject)
                .eq(KnowledgeUnitPO::getStatus, 0) // 仅预置状态
                .list();

        // 2. 构建AI提示词（要求返回existing和new_knowledge）
        String prompt = buildDualPrompt(target, subject, presetList);

        // 3. 调用AI服务
        Map<String, Object> aiResponse = callRAGService(prompt);

        // 4. 解析AI响应
        List<String> existingNames = (List<String>) aiResponse.get("existing");
        List<String> newKnowledgeNames = (List<String>) aiResponse.get("new_knowledge");

        // 5. 合并所有知识点（预置+新增）
        List<Map<String, Object>> allKnowledge = new ArrayList<>();

        // 5.1 处理预置知识点
        for (String name : existingNames) {
            presetList.stream()
                    .filter(po -> po.getName().equals(name))
                    .findFirst()
                    .ifPresent(po -> allKnowledge.add(Map.of(
                            "id", po.getId(),
                            "name", name,
                            "source", "existing"
                    )));
        }

        // 5.2 处理新增知识点
        List<KnowledgeUnitPO> savedList = saveNewKnowledgeWithCheck(newKnowledgeNames, subject, presetList);
        savedList.forEach(po -> allKnowledge.add(Map.of(
                "id", po.getId(),
                "name", po.getName(),
                "source", "new"
        )));

        // 6. 返回完整结果
        return Map.of(
                "target", target,
                "subject", subject,
                "preset_count", presetList.size(),
                "new_count", savedList.size(),
                "knowledge_units", allKnowledge
        );
    }

    // 构建双重提示词
    private String buildDualPrompt(String target, String subject, List<KnowledgeUnitPO> presetList) {
        String presetNames = presetList.stream()
                .map(KnowledgeUnitPO::getName)
                .collect(Collectors.joining("、"));

        return String.format(
                "你是一个%s科目的教学专家。你的服务对象是大学生。请分析他的学习目标【%s】涉及的最小粒度知识单元，要求：\n"
                        + "1. 将目标拆解为原子级知识点（如'三角函数'拆为'正弦定理'、'余弦定理'）,优先使用预置知识点,且拆解出的新增知识点不得相互包含，不得跟预置知识点重复。新增知识点不应过多，最多不应超过15个\n"
                        + "2. 返回JSON格式：{\"existing\": [\"知识点1\", ...], \"new_knowledge\": [\"知识点1\", ...]}\n"
                        + "3. 对于预置知识点列表【%s】中已存在的知识点，放入existing数组\n"
                        + "4. 不在预置列表中的知识点，放入new_knowledge数组\n"
                        + "5. 每个知识点必须是独立概念且不超过8个汉字\n"
                        + "6. 禁止出现包含关系（如'几何'和'三角形'不能同时存在）",
                subject, target, presetNames
        );
    }

    // 带唯一性检查的新增存储
    private List<KnowledgeUnitPO> saveNewKnowledgeWithCheck(
            List<String> newNames,
            String subject,
            List<KnowledgeUnitPO> presetList) {
        if (newNames == null || newNames.isEmpty()) {
            return Collections.emptyList();
        }

        // 获取所有已存在名称（预置+数据库）
        Set<String> allExistingNames = presetList.stream()
                .map(KnowledgeUnitPO::getName)
                .collect(Collectors.toSet());

        // 检查数据库唯一性（包括所有状态）
        Set<String> dbExistingNames = this.lambdaQuery()
                .select(KnowledgeUnitPO::getName)
                .eq(KnowledgeUnitPO::getSubject, subject)
                .list()
                .stream()
                .map(KnowledgeUnitPO::getName)
                .collect(Collectors.toSet()); // 直接使用Set接收

        allExistingNames.addAll(dbExistingNames);

        // 过滤有效新名称
        List<KnowledgeUnitPO> validEntities = newNames.stream()
                .filter(name -> !allExistingNames.contains(name))
                .map(name -> KnowledgeUnitPO.builder()
                        .name(name)
                        .subject(subject)
                        .status(1) // AI待审状态
                        .createdAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        if (!validEntities.isEmpty()) {
            this.saveBatch(validEntities);
        }

        return validEntities;
    }
    private Map<String, Object> callRAGService(String prompt) {
        // 1. 构建符合RAG接口的请求体（添加格式约束）
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        // 新增响应格式约束
        Map<String, Object> responseFormat = new HashMap<>();
        responseFormat.put("type", "json_object"); // 强制要求纯JSON输出
        requestBody.put("response_format", responseFormat);

        requestBody.put("messages", Collections.singletonList(message));

        // 2. 调用RAG服务（异步非阻塞）
        try {
            Map<String, Object> response = webClient.post()
                    .uri("/chat/plain")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .timeout(Duration.ofSeconds(120)) // 设置超时
                    .onErrorResume(TimeoutException.class, e -> {
                        log.error("RAG服务响应超时", e);
                        return Mono.just(Map.of("error", "AI服务响应超时"));
                    })
                    .block(); // 阻塞等待但已处理超时

            // 3. 预处理响应数据
            String rawResponse = (String) response.get("answer");
            if (rawResponse == null) {
                throw new RuntimeException("RAG服务返回空响应");
            }

            // 4. 清洗JSON响应（去除Markdown标记）
            String cleanedJson = cleanJsonResponse(rawResponse);

            // 5. 解析JSON（增强容错机制）
            return parseJsonResponse(cleanedJson);
        } catch (Exception e) {
            throw new RuntimeException("调用RAG服务失败: " + e.getMessage(), e);
        }
    }

    // JSON响应清洗方法
    private String cleanJsonResponse(String rawResponse) {
        // 移除Markdown代码块标记（开头```json和结尾```）
        return rawResponse.replaceAll("^```json\\s*", "")
                .replaceAll("```$", "")
                .trim();
    }

    // 健壮的JSON解析方法
    private Map<String, Object> parseJsonResponse(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        // 启用容错解析模式
        mapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        mapper.enable(JsonParser.Feature.IGNORE_UNDEFINED);

        try {
            return mapper.readValue(jsonString, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            // 尝试修复常见格式错误
            String repairedJson = jsonString
                    .replace("'", "\"") // 单引号转双引号
                    .replaceAll(",\\s*}", "}"); // 删除尾部多余逗号

            try {
                return mapper.readValue(repairedJson, new TypeReference<>() {});
            } catch (JsonProcessingException ex) {
                throw new RuntimeException("AI响应解析失败，原始内容: " + jsonString, ex);
            }
        }
    }








    @Override
    public Map<String, Object> addKnowledgeUnit(KnowledgeUnitBO knowledgeUnitBO) {

        knowledgeUnitMapper.insert(KnowledgeUnitBO.toKnowledgeUnitPO(knowledgeUnitBO));
        return Map.of("插入完成",KnowledgeUnitBO.toKnowledgeUnitPO(knowledgeUnitBO).getName());
    }

    @Override
    public KnowledgeUnitBO getKnowledgeUnitById(Long id) {
        return KnowledgeUnitBO.fromKnowledgeUnitPO(knowledgeUnitMapper.selectById(id));
    }

    @Override
    public Map<String, Object> batchAddKnowledgeUnits(List<KnowledgeUnitBO> boList) {
        List<String> successNames = new ArrayList<>();
        List<String> failedNames = new ArrayList<>();

        for (KnowledgeUnitBO bo : boList) {
            KnowledgeUnitPO po = KnowledgeUnitBO.toKnowledgeUnitPO( bo);
            po.setId(null); // 确保触发自增ID

            try {
                // 插入前校验唯一性（可选）
                if (!lambdaQuery().eq(KnowledgeUnitPO::getName, po.getName()).exists()) {
                    save(po);
                    successNames.add(po.getName());
                } else {
                    failedNames.add(po.getName()); // 跳过已存在记录
                }
            } catch (DuplicateKeyException e) {
                failedNames.add(po.getName());
            }
        }

        return Map.of(
                "successCount", successNames.size(),
                "successNames", successNames,
                "failedCount", failedNames.size(),
                "failedNames", failedNames
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchUpdateStatus(Integer status, List<Long> ids) {
        // ▼▼▼ 参数校验（确保必要参数有效）▼▼▼
        if (status == null) {
            throw new IllegalArgumentException("状态值不能为空");
        }
        if (ids == null || ids.isEmpty()) {
            return Map.of(
                    "successCount", 0,
                    "failedCount", 0,
                    "successIds", Collections.emptyList(),
                    "failedIds", Collections.emptyList()
            );
        }

        // ▼▼▼ 准备待更新实体列表 ▼▼▼
        List<KnowledgeUnitPO> updateList = ids.stream()
                .map(id -> {
                    KnowledgeUnitPO po = new KnowledgeUnitPO();
                    po.setId(id);
                    po.setStatus(status);
                    return po;
                })
                .collect(Collectors.toList());

        // ▼▼▼ MyBatis-Plus批量更新（500条/批次）▼▼▼
        boolean updateResult = updateBatchById(updateList, 500);

        // ▼▼▼ 返回结果统计 ▼▼▼
        return Map.of(
                "successCount", updateResult ? ids.size() : 0,
                "failedCount", updateResult ? 0 : ids.size(),
                "successIds", updateResult ? ids : Collections.emptyList(),
                "failedIds", updateResult ? Collections.emptyList() : ids
        );
    }

    @Override
    public List<KnowledgeUnitBO> searchKnowledgeUnit(String keyword) {
        // 1. 构建正确的模糊查询条件（分组OR条件）
        LambdaQueryWrapper<KnowledgeUnitPO> wrapper = Wrappers.lambdaQuery();
        wrapper.and(q ->
                q.like(KnowledgeUnitPO::getName, keyword)
                        .or()
                        .like(KnowledgeUnitPO::getSubject, keyword)
        );

        // 2. 执行查询并转换结果
        List<KnowledgeUnitPO> poList = knowledgeUnitMapper.selectList(wrapper);
        return poList.stream()
                .map(KnowledgeUnitBO::fromKnowledgeUnitPO)
                .collect(Collectors.toList());
    }


    @Override
    public Map<String, Object> batchDelete(List<Long> ids) {
        boolean success = this.removeByIds(ids); // 调用MP的removeByIds
        return Map.of("success", success, "deletedIds", ids);
    }


    @Override
    public Map<String, Object> getByStatusPage(Integer status, Integer current, Integer size) {
        // 1. 准备查询参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("status", status);

        // 2. 构建分页参数
        PageQueryHelper.buildPageQuery(paramMap, current, size);

        // 3. 执行分页查询
        List<KnowledgeUnitPO> records = knowledgeUnitMapper.selectByStatusPage(paramMap);

        // 4. 获取总记录数
        long total = knowledgeUnitMapper.countByStatus(paramMap);

        // 5. 转换为BO列表
        List<KnowledgeUnitBO> boList = records.stream()
                .map(KnowledgeUnitBO::fromKnowledgeUnitPO)
                .collect(Collectors.toList());

        // 6. 返回分页结果
        return Map.of(
                "status", status,
                "current", current,
                "size", size,
                "total", total,
                "pages", PageQueryHelper.calculatePages(total, size),
                "data", boList
        );
    }

    // 参数验证方法
    private void validateParameters(Integer status, Integer current, Integer size) {
        if (status == null || status < 0 || status > 2) {
            throw new IllegalArgumentException("状态值不合法");
        }
        if (current == null || current < 1) {
            throw new IllegalArgumentException("页码不能小于1");
        }
        if (size == null || size < 1 || size > 100) {
            throw new IllegalArgumentException("每页大小应在1-100之间");
        }
    }

    // 通用分页查询方法（按学科+状态过滤）
    @Override
    public Map<String, Object> getBySubjectAndStatusPage(
            String subject, Integer status, Integer current, Integer size) {

        // 1. 准备查询参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("subject", subject);
        paramMap.put("status", status);

        // 2. 构建分页参数
        PageQueryHelper.buildPageQuery(paramMap, current, size);

        // 3. 执行分页查询
        List<KnowledgeUnitPO> records = knowledgeUnitMapper.selectBySubjectAndStatusPage(paramMap);

        // 4. 获取总记录数
        long total = knowledgeUnitMapper.countBySubjectAndStatus(paramMap);

        // 5. 转换为BO列表
        List<KnowledgeUnitBO> boList = records.stream()
                .map(KnowledgeUnitBO::fromKnowledgeUnitPO)
                .collect(Collectors.toList());

        // 6. 返回分页结果
        return Map.of(
                "subject", subject,
                "current", current,
                "size", size,
                "total", total,
                "pages", PageQueryHelper.calculatePages(total, size),
                "data", boList
        );
    }

    @Override
    public List<KnowledgeUnitBO> getAllKnowledgeUnitsBySubject(String subject) {
        return this.lambdaQuery()
                .eq(KnowledgeUnitPO::getSubject, subject)
                .list()
                .stream()
                .map(KnowledgeUnitBO::fromKnowledgeUnitPO)
                .collect(Collectors.toList());
    }
}
