package com.codereview.reviewer.entity;

import com.codereview.common.audit.Auditable;
import com.codereview.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reviewer extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewerId;

  private String position;

  @ElementCollection
  @CollectionTable(joinColumns = @JoinColumn(name = "REVIEWER_ID"))
  private Set<String> skills;

  @Column(columnDefinition = "TEXT")
  private String introduction;

  @OneToOne
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  private Career career;
}
