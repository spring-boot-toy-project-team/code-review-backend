package com.codereview.comment.controller;


import com.codereview.comment.dto.CommentRequestDto;
import com.codereview.comment.entity.Comment;
import com.codereview.comment.mapper.CommentMapper;
import com.codereview.comment.service.CommentService;
import com.codereview.common.dto.response.MultiResponseWithPageInfoDto;
import com.codereview.common.dto.response.SingleResponseWithMessageDto;
import com.codereview.common.helper.RestPage;
import com.codereview.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping("/board/{board-id}/comments")
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

  /**
   * 댓글 등록
   */
  @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
  @PostMapping
  public ResponseEntity<SingleResponseWithMessageDto> createComment(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                    @Positive @PathVariable("board-id") long boardId,
                                                                    @Valid @RequestBody CommentRequestDto.CommentDto commentDto){
    commentDto.setMemberId(customUserDetails.getMember().getMemberId());
    commentDto.setBoardId(boardId);
    Comment comment = commentService.createComment(mapper.createCommentDtoToComment(commentDto));

    return new ResponseEntity<>(new SingleResponseWithMessageDto<>(mapper.commentToCommentInfo(comment),
            "SUCCESS"),
            HttpStatus.CREATED);
  }

  /**
   * 댓글 수정
   */
  @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
  @PatchMapping("/{comment-id}")
  public ResponseEntity<SingleResponseWithMessageDto> updateComment(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                    @Positive @PathVariable("board-id") long boardId,
                                                                    @Positive @PathVariable("comment-id") long commendId,
                                                                    @Valid @RequestBody CommentRequestDto.updateCommentDto updateCommentDto){
    updateCommentDto.setMemberId(customUserDetails.getMember().getMemberId());
    updateCommentDto.setBoardId(boardId);
    updateCommentDto.setCommentId(commendId);
    Comment comment = commentService.updateComment(mapper.updateCommentToComment(updateCommentDto));

    return new ResponseEntity<>(new SingleResponseWithMessageDto<>(mapper.commentToCommentInfo(comment),
            "SUCCESS"),
            HttpStatus.OK);
  }
}
