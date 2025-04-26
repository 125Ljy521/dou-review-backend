package com.doureview;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.doureview.**.mapper")
@SpringBootApplication
public class DouReviewApplication {
  public static void main(String[] args) {
    SpringApplication.run(DouReviewApplication.class, args);
  }
}