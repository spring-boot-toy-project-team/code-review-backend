package com.codereview.repostiory;

import com.codereview.img.repository.ImageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

@DataJpaTest
public class ImageRepositoryTest {
  @Autowired
  private ImageRepository imageRepository;

  @Test
  public void test() throws Exception {
    LocalDateTime time = LocalDateTime.now();
    System.out.println(time.getYear());
    System.out.println(time.getMonth());
    System.out.println(time.getDayOfMonth());
    System.out.println(time.getDayOfWeek());
  }
}
