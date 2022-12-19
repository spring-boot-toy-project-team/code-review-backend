package com.codereview.service;

import com.codereview.comment.entity.Comment;
import com.codereview.comment.repository.CommentRepository;
import com.codereview.comment.service.CommentService;
import com.codereview.stub.CommentStubData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

  @Spy
  @InjectMocks
  private CommentService commentService;
  @Mock
  private CommentRepository commentRepository;

  @Test
  @DisplayName("댓글 저장 성공 테스트")
  public void saveSuccessForComment(){
    //given
    Comment comment = CommentStubData.comment();

    given(commentRepository.save(Mockito.any(Comment.class))).willReturn(comment);

    //when
    Comment savedComment = commentService.createComment(comment);

    //then
    assertThat(comment).isEqualTo(savedComment);
  }


}
