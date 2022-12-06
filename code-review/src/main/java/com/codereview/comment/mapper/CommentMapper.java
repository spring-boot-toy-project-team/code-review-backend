package com.codereview.comment.mapper;

import com.codereview.comment.dto.CommentResponseDto;
import com.codereview.comment.entity.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
  List<CommentResponseDto.CommentDto> commentListToCommentDtoList(List<Comment> commentList);
}
