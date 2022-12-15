package com.codereview.stub;

import com.codereview.board.entity.Board;
import com.codereview.comment.dto.CommentRequestDto;
import com.codereview.comment.dto.CommentResponseDto;
import com.codereview.comment.entity.Comment;
import com.codereview.common.helper.RestPage;
import com.codereview.member.entity.Member;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentStubData {

  private final static Member member = MemberStubData.member();
  private final static Board board = BoardStubData.board();

  public static Comment comment(){
    return Comment.builder()
            .commentId(1L)
            .contents("댓글댓글")
            .member(member)
            .board(board)
            .build();
  }

  public static Comment comment(long commentId, String contents){
    return Comment.builder()
            .commentId(commentId)
            .contents(contents)
            .member(member)
            .board(board)
            .build();
  }

  public static CommentRequestDto.CommentDto commentDto(){
    return CommentRequestDto.CommentDto.builder()
            .boardId(1L)
            .memberId(1L)
            .contents("댓글댓글")
            .build();
  }

  public static Comment createCommentDtoToComment(CommentRequestDto.CommentDto commentDto){
    return Comment.builder()
            .member(member)
            .board(board)
            .contents(comment().getContents())
            .build();
  }

  public static CommentResponseDto.CommentInfoDto commentInfoDto(Comment comment){
    return CommentResponseDto.CommentInfoDto.builder()
            .commentId(comment().getCommentId())
            .contents(comment().getContents())
            .nickName(comment.getMember().getNickName())
            .createdAt(LocalDateTime.now())
            .modifiedAt(LocalDateTime.now())
            .build();
  }

  public static CommentRequestDto.updateCommentDto updateCommentDto(long commentId){
    return CommentRequestDto.updateCommentDto.builder()
            .boardId(board.getBoardId())
            .commentId(commentId)
            .contents("댓글 수정")
            .memberId(member.getMemberId())
            .build();

  }

  public static Comment updateCommentToComment(CommentRequestDto.updateCommentDto updateCommentDto){
    return Comment.builder()
            .board(board)
            .member(member)
            .commentId(updateCommentDto.getCommentId())
            .contents(updateCommentDto.getContents())
            .build();
  }

  public static RestPage<Comment> CommentRestPage(int page, int size){
    return new RestPage<>(new PageImpl<>(List.of(
            comment(1L, "댓글1"),
            comment(2L, "댓글2"),
            comment(3L, "댓글3")
    ), PageRequest.of(page, size, Sort.by("commentId").descending()),3));
  }

  public static List<CommentResponseDto.CommentInfoDto> CommentListToCommentInfoList(List<Comment> comments){
    return comments.stream().map(CommentStubData::commentInfoDto).collect(Collectors.toList());
  }
}
