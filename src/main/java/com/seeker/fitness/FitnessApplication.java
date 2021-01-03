package com.seeker.fitness;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.seeker.fitness.all.mapper")
public class FitnessApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitnessApplication.class, args);
    }
}
