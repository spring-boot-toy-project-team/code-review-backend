package com.codereview.comment.service;

import com.codereview.comment.entity.Comment;
import com.codereview.comment.repository.CommentRepository;
import com.codereview.helper.RestPage;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;

  @Transactional(readOnly = true)
  @Cacheable(key = "{#page, #size}", value = "getComments")
  public RestPage<Comment> getComments(Pageable pageable) {
    return new RestPage<>(commentRepository.findAll(pageable));
  }
}
