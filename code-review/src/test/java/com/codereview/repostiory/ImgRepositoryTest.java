package com.codereview.repostiory;

import com.codereview.config.TestConfig;
import com.codereview.img.entity.ImgType;
import com.codereview.img.repository.ImgRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import({TestConfig.class})
@DataJpaTest
public class ImgRepositoryTest {
  @Autowired
  private ImgRepository imgRepository;

  @Test
  public void test() throws Exception {

  }
}
