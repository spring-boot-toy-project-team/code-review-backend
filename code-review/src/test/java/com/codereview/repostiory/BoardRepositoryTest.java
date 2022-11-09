package com.codereview.repostiory;

import com.codereview.board.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BoardRepositoryTest {
  @Autowired
  private BoardRepository boardRepository;

  @Test
  @DisplayName("게시판 조회 테스트")
  void findAllTest() throws Exception {
    // given
    int page = 1;
    int size = 10;

    // when

    // then
  }
}
