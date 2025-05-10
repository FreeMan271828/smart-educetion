package org.nuist;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StudentApplication {
    public static void main( String[] args ) {
        SpringApplication.run(StudentApplication.class, args);
    }
}
