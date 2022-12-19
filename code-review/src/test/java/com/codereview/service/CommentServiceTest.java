package com.codereview.service;

import com.codereview.comment.repository.CommentRepository;
import com.codereview.comment.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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
    //when
    //then
  }
}
