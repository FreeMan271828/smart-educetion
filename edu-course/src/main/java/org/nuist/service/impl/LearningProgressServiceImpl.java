package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nuist.bo.LearningProgressBO;
import org.nuist.mapper.LearningProgressMapper;
import org.nuist.po.LearningProgressPO;
import org.nuist.service.LearningProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 学习进度服务实现类
 */
@Service
public class LearningProgressServiceImpl implements LearningProgressService {
    
    @Autowired
    private LearningProgressMapper learningProgressMapper;
    
    @Override
    public List<LearningProgressBO> getStudentProgress(Long studentId) {
        if (studentId == null) {
            return new ArrayList<>();
        }
        
        LambdaQueryWrapper<LearningProgressPO> queryWrapper = Wrappers.<LearningProgressPO>lambdaQuery()
                .eq(LearningProgressPO::getStudentId, studentId);
        
        List<LearningProgressPO> poList = learningProgressMapper.selectList(queryWrapper);
        return convertToBOList(poList);
    }
    
    @Override
    public List<LearningProgressBO> getStudentCourseProgress(Long studentId, Long courseId) {
        if (studentId == null || courseId == null) {
            return new ArrayList<>();
        }
        
        LambdaQueryWrapper<LearningProgressPO> queryWrapper = Wrappers.<LearningProgressPO>lambdaQuery()
                .eq(LearningProgressPO::getStudentId, studentId)
                .eq(LearningProgressPO::getCourseId, courseId);
        
        List<LearningProgressPO> poList = learningProgressMapper.selectList(queryWrapper);
        return convertToBOList(poList);
    }
    
    @Override
    public List<LearningProgressBO> getStudentCourseProgressByName(Long studentId, String courseName) {
        if (studentId == null || !StringUtils.hasText(courseName)) {
            return new ArrayList<>();
        }
        
        List<LearningProgressPO> poList = learningProgressMapper.selectByStudentId(studentId);
        return convertToBOList(poList);
    }
    
    @Override
    public LearningProgressBO getStudentKnowledgeProgress(Long studentId, Long knowledgeId) {
        if (studentId == null || knowledgeId == null) {
            return null;
        }
        
        LambdaQueryWrapper<LearningProgressPO> queryWrapper = Wrappers.<LearningProgressPO>lambdaQuery()
                .eq(LearningProgressPO::getStudentId, studentId)
                .eq(LearningProgressPO::getKnowledgeId, knowledgeId);
        
        LearningProgressPO po = learningProgressMapper.selectOne(queryWrapper);
        return po != null ? LearningProgressBO.fromPO(po) : null;
    }
    
    @Override
    public List<LearningProgressBO> getStudentKnowledgeProgressByName(Long studentId, String knowledgeName) {
        if (studentId == null || !StringUtils.hasText(knowledgeName)) {
            return new ArrayList<>();
        }
        
        List<LearningProgressPO> poList = learningProgressMapper.selectByStudentIdAndKnowledgeName(studentId, "%" + knowledgeName + "%");
        return convertToBOList(poList);
    }
    
    @Override
    @Transactional
    public Long updateLearningProgress(LearningProgressBO progressBO) {
        if (progressBO == null || progressBO.getStudentId() == null || 
                progressBO.getCourseId() == null || progressBO.getKnowledgeId() == null) {
            return null;
        }
        
        LearningProgressPO po = progressBO.toPO();
        LocalDateTime now = LocalDateTime.now();
        
        if (po.getProgressId() == null) {
            // 新增
            po.setCreatedAt(now);
            po.setUpdatedAt(now);
            Integer id = learningProgressMapper.insert(po);
            po.setStudentId(Long.valueOf(id));
        } else {
            // 更新
            po.setUpdatedAt(now);
            learningProgressMapper.updateById(po);
        }
        
        return po.getProgressId();
    }
    
    @Override
    @Transactional
    public int batchUpdateLearningProgress(List<LearningProgressBO> progressList) {
        if (progressList == null || progressList.isEmpty()) {
            return 0;
        }
        
        LocalDateTime now = LocalDateTime.now();
        List<LearningProgressPO> poList = progressList.stream()
                .filter(bo -> bo.getStudentId() != null && bo.getCourseId() != null && bo.getKnowledgeId() != null)
                .map(bo -> {
                    LearningProgressPO po = bo.toPO();
                    if (po.getProgressId() == null) {
                        po.setCreatedAt(now);
                    }
                    po.setUpdatedAt(now);
                    return po;
                })
                .collect(Collectors.toList());
        
        if (poList.isEmpty()) {
            return 0;
        }
        learningProgressMapper.insert(poList);
        return poList.size();
    }
    
    @Override
    @Transactional
    public LearningProgressBO updateMasteryLevel(Long studentId, Long knowledgeId, BigDecimal score) {
        if (studentId == null || knowledgeId == null || score == null) {
            return null;
        }
        
        // 查找现有进度
        LearningProgressBO progress = getStudentKnowledgeProgress(studentId, knowledgeId);
        
        if (progress == null) {
            // 不存在进度记录，创建新记录
            progress = new LearningProgressBO();
            progress.setStudentId(studentId);
            progress.setKnowledgeId(knowledgeId);
            progress.setMasteryLevel(score);
            progress.setReviewCount(1);
            progress.setLearningStatus("in_progress");
            progress.setLastReviewedAt(LocalDateTime.now());
        } else {
            // 更新现有进度
            progress.updateMasteryLevel(score);
            
            // 根据掌握度更新学习状态
            if (progress.isMastered()) {
                progress.setLearningStatus("mastered");
            } else {
                progress.setLearningStatus("in_progress");
            }
        }
        
        // 保存更新
        updateLearningProgress(progress);
        
        return progress;
    }
    
    @Override
    @Transactional
    public LearningProgressBO updateMasteryLevelByName(Long studentId, String knowledgeName, BigDecimal score) {
        if (studentId == null || !StringUtils.hasText(knowledgeName) || score == null) {
            return null;
        }
        
        // 通过知识点名称查找知识点ID
        // 实际实现需要从知识点表中查找ID
        // 这里简化处理，假设通过其他服务获取知识点ID
        Long knowledgeId = findKnowledgeIdByName(knowledgeName);
        
        if (knowledgeId == null) {
            return null;
        }
        
        return updateMasteryLevel(studentId, knowledgeId, score);
    }
    
    @Override
    public BigDecimal getOverallProgress(Long studentId) {
        if (studentId == null) {
            return BigDecimal.ZERO;
        }
        
        // 获取总知识点数量和已掌握知识点数量
        int totalKnowledgePoints = getTotalKnowledgePointsCount(studentId);
        if (totalKnowledgePoints == 0) {
            return BigDecimal.ZERO;
        }
        
        int masteredCount = learningProgressMapper.countMasteredKnowledgePoints(studentId);
        
        // 计算进度百分比
        return new BigDecimal(masteredCount)
                .divide(new BigDecimal(totalKnowledgePoints), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100));
    }
    
    @Override
    public BigDecimal getCourseProgress(Long studentId, Long courseId) {
        if (studentId == null || courseId == null) {
            return BigDecimal.ZERO;
        }
        
        // 获取课程总知识点数量和已掌握知识点数量
        int totalCourseKnowledgePoints = getTotalKnowledgePointsCountByCourse(studentId, courseId);
        if (totalCourseKnowledgePoints == 0) {
            return BigDecimal.ZERO;
        }
        
        int masteredCount = learningProgressMapper.countMasteredKnowledgePointsByCourse(studentId, courseId);
        
        // 计算进度百分比
        return new BigDecimal(masteredCount)
                .divide(new BigDecimal(totalCourseKnowledgePoints), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100));
    }
    
    @Override
    public BigDecimal getCourseProgressByName(Long studentId, String courseName) {
        if (studentId == null || !StringUtils.hasText(courseName)) {
            return BigDecimal.ZERO;
        }
        
        // 通过课程名称查找课程ID
        Long courseId = findCourseIdByName(courseName);
        
        if (courseId == null) {
            return BigDecimal.ZERO;
        }
        
        return getCourseProgress(studentId, courseId);
    }
    
    @Override
    public Map<String, Object> getProgressStatistics(Long studentId) {
        if (studentId == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> statistics = new HashMap<>();
        
        int totalKnowledgePoints = getTotalKnowledgePointsCount(studentId);
        int masteredCount = learningProgressMapper.countMasteredKnowledgePoints(studentId);
        int inProgressCount = learningProgressMapper.countInProgressKnowledgePoints(studentId);
        int notStartedCount = totalKnowledgePoints - masteredCount - inProgressCount;
        
        statistics.put("studentId", studentId);
        statistics.put("totalKnowledgePoints", totalKnowledgePoints);
        statistics.put("masteredCount", masteredCount);
        statistics.put("inProgressCount", inProgressCount);
        statistics.put("notStartedCount", notStartedCount);
        statistics.put("overallProgressPercent", getOverallProgress(studentId));
        
        return statistics;
    }
    
    @Override
    public Map<String, Object> getProgressStatisticsByCourseName(Long studentId, String courseName) {
        if (studentId == null || !StringUtils.hasText(courseName)) {
            return new HashMap<>();
        }
        
        // 通过课程名称查找课程ID
        Long courseId = findCourseIdByName(courseName);
        
        if (courseId == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> statistics = new HashMap<>();
        
        int totalKnowledgePoints = getTotalKnowledgePointsCountByCourse(studentId, courseId);
        int masteredCount = learningProgressMapper.countMasteredKnowledgePointsByCourse(studentId, courseId);
        int inProgressCount = learningProgressMapper.countInProgressKnowledgePointsByCourse(studentId, courseId);
        int notStartedCount = totalKnowledgePoints - masteredCount - inProgressCount;
        
        statistics.put("studentId", studentId);
        statistics.put("courseId", courseId);
        statistics.put("courseName", courseName);
        statistics.put("totalKnowledgePoints", totalKnowledgePoints);
        statistics.put("masteredCount", masteredCount);
        statistics.put("inProgressCount", inProgressCount);
        statistics.put("notStartedCount", notStartedCount);
        statistics.put("courseProgressPercent", getCourseProgress(studentId, courseId));
        
        return statistics;
    }
    
    @Override
    public Map<String, Object> generateLearningPlan(Long studentId, Long courseId, Integer days) {
        if (studentId == null || courseId == null || days == null || days <= 0) {
            return new HashMap<>();
        }
        
        Map<String, Object> learningPlan = new HashMap<>();
        
        // 获取课程中未掌握的知识点
        List<Map<String, Object>> notMasteredPoints = learningProgressMapper.selectNotMasteredKnowledgePointsByCourse(studentId, courseId);
        
        if (notMasteredPoints.isEmpty()) {
            learningPlan.put("message", "课程已全部掌握，无需制定学习计划");
            return learningPlan;
        }
        
        // 简单的学习计划生成逻辑，平均分配知识点
        int totalPoints = notMasteredPoints.size();
        int pointsPerDay = (int) Math.ceil((double) totalPoints / days);
        
        List<Map<String, Object>> dailyPlans = new ArrayList<>();
        int currentPoint = 0;
        
        for (int day = 1; day <= days; day++) {
            Map<String, Object> dailyPlan = new HashMap<>();
            dailyPlan.put("day", day);
            
            List<Map<String, Object>> todayPoints = new ArrayList<>();
            for (int i = 0; i < pointsPerDay && currentPoint < totalPoints; i++) {
                todayPoints.add(notMasteredPoints.get(currentPoint++));
            }
            
            dailyPlan.put("knowledgePoints", todayPoints);
            dailyPlan.put("pointsCount", todayPoints.size());
            dailyPlans.add(dailyPlan);
            
            if (currentPoint >= totalPoints) {
                break;
            }
        }
        
        learningPlan.put("studentId", studentId);
        learningPlan.put("courseId", courseId);
        learningPlan.put("totalDays", days);
        learningPlan.put("totalKnowledgePoints", totalPoints);
        learningPlan.put("dailyPlans", dailyPlans);
        
        return learningPlan;
    }
    
    @Override
    public Map<String, Object> generateLearningPlanByCourseName(Long studentId, String courseName, Integer days) {
        if (studentId == null || !StringUtils.hasText(courseName) || days == null || days <= 0) {
            return new HashMap<>();
        }
        
        // 通过课程名称查找课程ID
        Long courseId = findCourseIdByName(courseName);
        
        if (courseId == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "找不到课程: " + courseName);
            return errorResponse;
        }
        
        return generateLearningPlan(studentId, courseId, days);
    }
    
    @Override
    public List<Map<String, Object>> getRecommendedResources(Long studentId, Integer limit) {
        if (studentId == null) {
            return new ArrayList<>();
        }
        
        // 设置默认值
        int actualLimit = limit != null && limit > 0 ? limit : 10;
        
        return learningProgressMapper.selectRecommendedResourcesForStudent(studentId, actualLimit);
    }
    
    @Override
    public List<Map<String, Object>> searchRecommendedResources(Long studentId, String keywords, Integer limit) {
        if (studentId == null || !StringUtils.hasText(keywords)) {
            return new ArrayList<>();
        }
        
        // 设置默认值
        int actualLimit = limit != null && limit > 0 ? limit : 10;
        
        return learningProgressMapper.searchRecommendedResourcesForStudent(studentId, "%" + keywords + "%", actualLimit);
    }
    
    /**
     * 将PO列表转换为BO列表
     * @param poList PO列表
     * @return BO列表
     */
    private List<LearningProgressBO> convertToBOList(List<LearningProgressPO> poList) {
        if (poList == null || poList.isEmpty()) {
            return new ArrayList<>();
        }
        
        return poList.stream()
                .map(LearningProgressBO::fromPO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取学生的总知识点数量
     * 实际实现需要从知识点表中查询
     * @param studentId 学生ID
     * @return 总知识点数量
     */
    private int getTotalKnowledgePointsCount(Long studentId) {
        // 模拟实现，实际应该查询数据库
        // 获取所有需要学习的知识点数量
        return 100; // 假设总共有100个知识点
    }
    
    /**
     * 获取课程的总知识点数量
     * 实际实现需要从知识点表中查询
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 课程的总知识点数量
     */
    private int getTotalKnowledgePointsCountByCourse(Long studentId, Long courseId) {
        // 模拟实现，实际应该查询数据库
        // 获取特定课程需要学习的知识点数量
        return 20; // 假设每个课程有20个知识点
    }
    
    /**
     * 通过课程名称查找课程ID
     * 实际实现需要从课程表中查询
     * @param courseName 课程名称
     * @return 课程ID
     */
    private Long findCourseIdByName(String courseName) {
        // 模拟实现，实际应该查询数据库
        // 假设这里是通过课程表查询课程ID
        return 1L; // 返回一个模拟的课程ID
    }
    
    /**
     * 通过知识点名称查找知识点ID
     * 实际实现需要从知识点表中查询
     * @param knowledgeName 知识点名称
     * @return 知识点ID
     */
    private Long findKnowledgeIdByName(String knowledgeName) {
        // 模拟实现，实际应该查询数据库
        // 假设这里是通过知识点表查询知识点ID
        return 1L; // 返回一个模拟的知识点ID
    }
} 