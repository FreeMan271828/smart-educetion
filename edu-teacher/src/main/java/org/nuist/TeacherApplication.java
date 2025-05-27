package org.nuist;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("org.nuist.mapper")
public class TeacherApplication {
    public static void main( String[] args ) {
        SpringApplication.run(TeacherApplication.class, args);
    }
}
