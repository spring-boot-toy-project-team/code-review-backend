package com.codereview.board.entity;

import com.codereview.tag.entity.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BoardTag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long boardTagId;

  @ManyToOne
  @JoinColumn(name = "BOARD_ID")
  private Board board;

  @ManyToOne
  @JoinColumn(name = "TAG_ID")
  private Tag tag;

  @Builder
  public BoardTag(Long boardTagId, Board board, Tag tag){
    this.boardTagId = boardTagId;
    this.board = board;
    this.tag = tag;
  }

}
