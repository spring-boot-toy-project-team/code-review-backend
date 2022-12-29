package com.codereview.service;

import com.codereview.comment.entity.Comment;
import com.codereview.comment.repository.CommentRepository;
import com.codereview.comment.service.CommentService;
import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.stub.CommentStubData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

  @Spy
  @InjectMocks
  private CommentService commentService;
  @Mock
  private CommentRepository commentRepository;

  @Test
  @DisplayName("댓글 저장 테스트")
  public void saveComment(){
    //given
    Comment comment = CommentStubData.comment();

    given(commentRepository.save(Mockito.any(Comment.class))).willReturn(comment);

    //when
    Comment savedComment = commentService.createComment(comment);

    //then
    assertThat(comment).isEqualTo(savedComment);
  }

  @Test
  @DisplayName("댓글 저장 실패 테스트")
  public void failedSaveComment() throws NoSuchMethodException {
    //given
    Comment comment = CommentStubData.comment();
    long commentId = 1L;
    long memberId = 2L;

    lenient().doReturn(comment).when(commentService).findVerifiedCommentWithMemberId(commentId,memberId);
    //when
    //then
    lenient().doThrow(new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND))
            .when(commentService).findVerifiedCommentWithMemberId(commentId,memberId);
  }

  @Test
  @DisplayName("댓글 삭제 테스트")
  public void deleteComment(){
    //given
    Comment comment = CommentStubData.comment();

    given(commentRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(comment));

    //when
    commentService.deleteCommentByIdAndMemberId(comment.getCommentId(),comment.getMember().getMemberId());

    //then
    Mockito.verify(commentRepository).delete(comment);
  }

}
