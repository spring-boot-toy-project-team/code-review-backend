package com.codereview.comment.service;

import com.codereview.board.entity.Board;
import com.codereview.comment.entity.Comment;
import com.codereview.comment.repository.CommentRepository;
import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.common.helper.RestPage;
import com.codereview.util.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;
  private final CustomBeanUtils<Comment> beanUtils;

  @Transactional(readOnly = true)
  @Cacheable(key = "{#page, #size}", value = "getComments")
  public RestPage<Comment> getComments(Pageable pageable) {
    return new RestPage<>(commentRepository.findAll(pageable));
  }

  /**
   * 댓글 작성
   */
  public Comment createComment(Comment comment) {
    return commentRepository.save(comment);
  }


}
