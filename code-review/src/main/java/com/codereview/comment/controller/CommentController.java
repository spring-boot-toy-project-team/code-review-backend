package com.codereview.comment.controller;


import com.codereview.comment.entity.Comment;
import com.codereview.comment.mapper.CommentMapper;
import com.codereview.comment.service.CommentService;
import com.codereview.common.dto.response.MultiResponseWithPageInfoDto;
import com.codereview.helper.RestPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
  private final CommentService commentService;
  private final CommentMapper mapper;

  /**
   * 댓글 리스트 조회
   */
  @GetMapping
  public ResponseEntity<MultiResponseWithPageInfoDto> getComments(
          @PageableDefault(size = 10, sort = "commentId", direction = Sort.Direction.DESC) Pageable pageable){
    RestPage<Comment> commentRestPage = commentService.getComments(pageable);
    List<Comment> commentList = commentRestPage.getContent();
    return new ResponseEntity<>(new MultiResponseWithPageInfoDto<>(mapper.commentListToCommentDtoList(commentList), commentRestPage),
            HttpStatus.OK);
  }


}
