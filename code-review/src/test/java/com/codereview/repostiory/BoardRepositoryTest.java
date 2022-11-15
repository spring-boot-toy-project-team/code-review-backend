package com.codereview.repostiory;

import com.codereview.board.entity.Board;
import com.codereview.board.entity.BoardTag;
import com.codereview.board.repository.board.BoardRepository;
import com.codereview.config.TestConfig;
import com.codereview.member.entity.Member;
import com.codereview.member.repository.MemberRepository;
import com.codereview.stub.MemberStubData;
import com.codereview.tag.entity.Tag;
import com.codereview.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({TestConfig.class})
@DataJpaTest
public class BoardRepositoryTest {
  @Autowired
  private BoardRepository boardRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private TagRepository tagRepository;

  private Member member;
  @BeforeEach
  void beforeEach() {
    member = memberRepository.save(MemberStubData.member());
  }

  @Test
  @DisplayName("게시판 저장 테스트")
  void saveBoardTest() throws Exception {
    // given
    Board board = Board.builder()
      .contents("내용")
      .title("이름")
      .member(member)
      .build();

    Tag tag = Tag.builder()
      .name("A")
      .build();

    tagRepository.save(tag);

    BoardTag boardTag = BoardTag.builder()
      .board(board)
      .tag(tag)
      .build();

    board.setBoardTags(List.of(boardTag));
    tag.setBoardTags(List.of(boardTag));

    // when
    Board savedBoard = boardRepository.save(board);

    // then
    assertThat(board).isEqualTo(savedBoard);
  }


}
