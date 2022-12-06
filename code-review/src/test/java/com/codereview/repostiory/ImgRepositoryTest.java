package com.codereview.repostiory;

import com.codereview.config.TestConfig;
import com.codereview.img.entity.Img;
import com.codereview.img.entity.ImgType;
import com.codereview.img.repository.ImgRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

@Import({TestConfig.class})
@DataJpaTest
public class ImgRepositoryTest {
  @Autowired
  private ImgRepository imgRepository;
  private Img savedImg;
  @BeforeEach
  public void beforeEach() throws Exception {
    Img img = Img.builder()
      .path("path")
      .uri("uri")
      .type(ImgType.BOARD)
      .build();

    savedImg = imgRepository.save(img);
  }

  @Test
  @DisplayName("img entity 저장 테스트")
  public void saveImgTest() throws Exception {
    // given
    Img img = Img.builder()
      .path("path")
      .uri("uri")
      .type(ImgType.BOARD)
      .build();

    // when
    Img savedImg = imgRepository.save(img);

    // then
    Assertions.assertThat(img).isEqualTo(savedImg);
  }

  @Test
  @DisplayName("이미지 아이디로 삭제 테스트")
  public void deleteByIdTest() throws Exception {
    // given
    Long imgId = savedImg.getImgId();

    // when
    imgRepository.deleteById(imgId);

    // then
    Optional<Img> optionalImg = imgRepository.findById(imgId);
    Assertions.assertThat(false).isEqualTo(optionalImg.isPresent());
  }
}
