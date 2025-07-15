package org.nuist.handlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 通用 PostgreSQL 枚举类型处理器
 *
 * 使用方式：
 * 1. 在 PO 类字段上添加注解：
 *    @TableField(value = "type", typeHandler = PostgresEnumTypeHandler.class)
 *    private String type;
 *
 * 2. 在 application.properties 中配置：
 *    mybatis-plus.configuration.default-enum-type-handler=org.nuist.handlers.PostgresEnumTypeHandler
 *
 * 3. 或者在 PO 类上添加注解：
 *    @TableName(value = "assignment", autoResultMap = true)
 */
@MappedTypes(String.class)
public class PostgresEnumTypeHandler extends BaseTypeHandler<String> {

    private String enumTypeName;

    /**
     * 默认构造函数（必须存在）
     */
    public PostgresEnumTypeHandler() {
        // 默认使用字段名作为枚举类型名
    }

    /**
     * 带枚举类型名的构造函数
     * @param enumTypeName PostgreSQL 枚举类型名称
     */
    public PostgresEnumTypeHandler(String enumTypeName) {
        this.enumTypeName = enumTypeName;
    }

    /**
     * 设置枚举类型名（用于动态设置）
     * @param enumTypeName PostgreSQL 枚举类型名称
     */
    public void setEnumTypeName(String enumTypeName) {
        this.enumTypeName = enumTypeName;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    String parameter, JdbcType jdbcType) throws SQLException {
        PGobject pgObject = new PGobject();

        // 确定枚举类型名
        String typeName = determineEnumTypeName(ps.getParameterMetaData().getParameterTypeName(i));

        pgObject.setType(typeName);
        pgObject.setValue(parameter);
        ps.setObject(i, pgObject);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }

    /**
     * 确定枚举类型名称
     *
     * 优先级：
     * 1. 构造函数传入的 enumTypeName
     * 2. 字段注解指定的类型
     * 3. 根据字段名推断的类型
     *
     * @param jdbcTypeName JDBC 类型名
     * @return 枚举类型名
     */
    private String determineEnumTypeName(String jdbcTypeName) {
        // 1. 如果构造函数指定了类型名，直接使用
        if (enumTypeName != null && !enumTypeName.isEmpty()) {
            return enumTypeName;
        }

        // 2. 尝试从 JDBC 类型名获取
        if (jdbcTypeName != null && jdbcTypeName.startsWith("enum_")) {
            return jdbcTypeName.substring(5); // 去掉 "enum_" 前缀
        }

        // 3. 根据常见字段名推断类型
        if ("type".equalsIgnoreCase(jdbcTypeName)) {
            return "assignment_type";
        } else if ("status".equalsIgnoreCase(jdbcTypeName)) {
            return "assignment_status";
        } else if ("problem_type".equalsIgnoreCase(jdbcTypeName)) {
            return "problem_type";
        } else if ("answer_status".equalsIgnoreCase(jdbcTypeName)) {
            return "answer_status";
        } else if ("grading_status".equalsIgnoreCase(jdbcTypeName)) {
            return "grading_status";
        }

        // 4. 默认使用字段名
        return jdbcTypeName;
    }
}