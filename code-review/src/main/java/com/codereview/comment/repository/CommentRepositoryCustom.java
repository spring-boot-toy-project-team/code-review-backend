package com.codereview.comment.repository;


import com.codereview.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

  Page<Comment> findAllByBoardId(long boardId, Pageable pageable);
}
