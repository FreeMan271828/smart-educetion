package org.nuist.config;

import org.nuist.factory.YamlPropertySourceFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:application-dev.yml", factory = YamlPropertySourceFactory.class)
public class SpringConfig {
}
