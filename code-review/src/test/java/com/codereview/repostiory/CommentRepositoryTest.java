package com.codereview.repostiory;

import com.codereview.board.entity.Board;
import com.codereview.board.repository.board.BoardRepository;
import com.codereview.comment.entity.Comment;
import com.codereview.comment.repository.CommentRepository;
import com.codereview.config.TestConfig;
import com.codereview.member.entity.Member;
import com.codereview.member.repository.MemberRepository;
import com.codereview.stub.BoardStubData;
import com.codereview.stub.MemberStubData;
import com.codereview.stub.TagStubData;
import com.codereview.tag.entity.Tag;
import com.codereview.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import({TestConfig.class})
@DataJpaTest
public class CommentRepositoryTest {
  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private BoardRepository boardRepository;

  private Member member;
  private Board board;
  @BeforeEach
  void memberBeforeEach() {
    member = memberRepository.save(MemberStubData.member());
    board = boardRepository.save(BoardStubData.board());
  }

  @Test
  @DisplayName("댓글 저장 테스트")
  void saveCommentsTest(){
    //given
    Comment comment = Comment.builder()
            .member(member)
            .board(board)
            .contents("댓글 저장")
            .build();
    //when
    Comment savedComment = commentRepository.save(comment);

    //then
    assertThat(comment).isEqualTo(savedComment);
  }
}
