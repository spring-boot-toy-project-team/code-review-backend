package com.codereview.img.entity;

import com.codereview.board.entity.Board;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class BoardImg {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long boardImgId;

  private String dir;

  private LocalDateTime createdAt = LocalDateTime.now();

  @ManyToOne
  @JoinColumn(name = "BOARD_ID")
  private Board board;

  @Builder
  public BoardImg(Long boardImgId, String dir){
    this.boardImgId = boardImgId;
    this.dir = dir;
  }
}
