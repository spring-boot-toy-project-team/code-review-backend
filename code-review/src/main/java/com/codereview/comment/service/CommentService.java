package com.codereview.comment.service;

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
  public RestPage<Comment> getComments(long boardId, Pageable pageable) {
    return new RestPage<>(commentRepository.findAllByBoardId(boardId,pageable));
  }

  /**
   * 댓글 작성
   */
  public Comment createComment(Comment comment) {
    return commentRepository.save(comment);
  }

  /**
   * 댓글 수정
   */
  public Comment updateComment(Comment comment) {
    Comment findComment = findVerifiedCommentWithMemberId(comment.getCommentId(), comment.getMember().getMemberId());
    Comment saveComment = beanUtils.copyNonNullProperties(comment, findComment);

    return commentRepository.save(saveComment);
  }

  /**
   * 댓글 삭제
   */

  public void deleteCommentByIdAndMemberId(long commendId, Long memberId) {
    Comment findComment = findVerifiedCommentWithMemberId(commendId, memberId);
    commentRepository.delete(findComment);
  }

  @Transactional(readOnly = true)
  private Comment findVerifiedComment(long commentId){
    Optional<Comment> optionalComment = commentRepository.findById(commentId);
    return optionalComment.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  private Comment findVerifiedCommentWithMemberId(long commentId, long memberId){
    Comment findComment = findVerifiedComment(commentId);
    if (findComment.getMember().getMemberId() != memberId){
      throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
    }
    return findComment;
  }


}
