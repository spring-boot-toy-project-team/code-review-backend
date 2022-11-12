package com.codereview.tag.entity;

import com.codereview.board.entity.BoardTag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Tag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long tagId;

  @Column(unique = true)
  private String name;

  @OneToMany(mappedBy = "tag")
  private List<BoardTag> boardTags;

  @Builder
  public Tag(Long tagId, String name, List<BoardTag> boardTags){
    this.tagId = tagId;
    this.name = name;
    this.boardTags = boardTags;
  }
}
