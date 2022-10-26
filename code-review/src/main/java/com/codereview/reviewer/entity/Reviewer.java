package com.codereview.reviewer.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Reviewer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewerId;

  private String position;

  @ElementCollection
  @CollectionTable(joinColumns = @JoinColumn(name = "REVIEWER_ID"))
  private Set<String> skills;

  private Career career;

  @Builder
  public Reviewer(Long reviewerId, String position, Set<String> skills, Career career) {
    this.reviewerId = reviewerId;
    this.position = position;
    this.skills = skills;
    this.career = career;
  }
}
