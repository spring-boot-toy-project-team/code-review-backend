package com.codereview.board.entity;

import com.codereview.common.audit.Auditable;
import com.codereview.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Board extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long boardId;

  private String title;

  @Column(columnDefinition = "TEXT")
  private String contents;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  @Builder
  public Board(Long boardId, String title, String contents){
    this.boardId = boardId;
    this.title = title;
    this.contents = contents;
  }

}
