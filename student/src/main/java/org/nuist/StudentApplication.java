package org.nuist;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("org.nuist.dao")
public class StudentApplication {
    public static void main( String[] args ) {
        SpringApplication.run(StudentApplication.class, args);
    }
}
