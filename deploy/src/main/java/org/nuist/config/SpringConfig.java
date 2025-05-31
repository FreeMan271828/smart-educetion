package org.nuist.config;

import org.mybatis.spring.annotation.MapperScan;
import org.nuist.factory.YamlPropertySourceFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@MapperScan("org.nuist.mapper")
@PropertySource(value = "classpath:application-dev.yml", factory = YamlPropertySourceFactory.class)
public class SpringConfig {
}
