package com.codereview.comment.mapper;

import com.codereview.board.entity.Board;
import com.codereview.comment.dto.CommentRequestDto;
import com.codereview.comment.dto.CommentResponseDto;
import com.codereview.comment.entity.Comment;
import com.codereview.member.entity.Member;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
  List<CommentResponseDto.CommentInfoDto> commentListToCommentDtoList(List<Comment> commentList);

  default Comment createCommentDtoToComment(CommentRequestDto.CommentDto commentDto){
    return Comment.builder()
            .member(Member.builder().memberId(commentDto.getMemberId()).build())
            .board(Board.builder().boardId(commentDto.getBoardId()).build())
            .contents(commentDto.getContents())
            .build();
  }

  default CommentResponseDto.CommentInfoDto commentToCommentInfo(Comment comment){
    return CommentResponseDto.CommentInfoDto.builder()
            .commentId(comment.getCommentId())
            .contents(comment.getContents())
            .createdAt(comment.getCreatedAt())
            .modifiedAt(comment.getModifiedAt())
            .build();
  }


}
