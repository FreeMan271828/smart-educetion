package org.nuist.config.springdoc;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchemaCustomizer implements PropertyCustomizer {
    @Override
    public Schema<?> customize(Schema property, AnnotatedType type) {
        Class<?> rawType = type.getType() instanceof Class ? (Class<?>) type.getType() : null;
        if ((rawType == Long.class || rawType == long.class)) {
            // 返回一个新的 String 类型的 Schema 以代替默认的 integer 类型
            return new StringSchema().example("1234567890123456789");
        }
        return property;
    }

    @Bean
    public OpenApiCustomizer longStringCustomizer() {
        return openApi -> {
            var components = openApi.getComponents();
            if (components != null && components.getSchemas() != null) {
                components.getSchemas().forEach((name, schema) -> {
                    if (schema != null) {
                        Schema<?> s = (Schema<?>) schema;
                        if ("integer".equals(s.getType()) && "int64".equals(s.getFormat())) {
                            s.setType("string");
                            s.setFormat(null);
                            s.setExample("1234567890123456789");
                        }
                    }
                });
            }
        };
    }
}
