package com.codereview.board.entity;

import com.codereview.common.audit.Auditable;
import com.codereview.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Board extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long boardId;

  private String title;

  @Column(columnDefinition = "TEXT")
  private String contents;

  @ManyToOne
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<BoardTag> boardTags;

  @Builder
  public Board(Long boardId, String title, String contents, Member member, List<BoardTag> boardTags){
    this.boardId = boardId;
    this.title = title;
    this.contents = contents;
    this.member = member;
    this.boardTags = boardTags;
  }

}
