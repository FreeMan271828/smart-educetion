package org.nuist.util;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
public class PageQueryHelper {

    /**
     * 构建分页查询条件
     * @param paramMap 查询参数Map
     * @param pageNum 当前页码
     * @param pageSize 每页大小
     */
    public static void buildPageQuery(Map<String, Object> paramMap, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        paramMap.put("offset", offset);
        paramMap.put("limit", pageSize);
    }

    /**
     * 计算总页数
     * @param total 总记录数
     * @param pageSize 每页大小
     */
    public static int calculatePages(long total, int pageSize) {
        return (int) Math.ceil((double) total / pageSize);
    }
}