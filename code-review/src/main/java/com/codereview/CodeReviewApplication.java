package com.codereview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
public class CodeReviewApplication {

  public static void main(String[] args) {
    SpringApplication.run(CodeReviewApplication.class, args);
  }

  @Bean
  public PageableHandlerMethodArgumentResolverCustomizer customize() {
    return p -> {
      p.setOneIndexedParameters(true);
    };
  }
}
