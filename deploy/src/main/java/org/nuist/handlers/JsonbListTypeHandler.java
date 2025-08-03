package org.nuist.handlers;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 将Postgresql Jsonb类型字段转换为String[]（适用于编程题用例持久化）
 */
@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.OTHER)
public class JsonbListTypeHandler extends JacksonTypeHandler {

    public JsonbListTypeHandler(Class<List<String>> type, Field field) {
        super(type, field);
    }

    public JsonbListTypeHandler() {
        super(List.class);
    }

    public JsonbListTypeHandler(Class<List<String>> type) {
        super(type);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        PGobject pg = new PGobject();
        pg.setType("jsonb");
        pg.setValue(toJson(parameter));
        ps.setObject(i, pg);
    }
}
