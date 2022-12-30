package com.codereview.review.entity;

import com.codereview.common.audit.Auditable;
import com.codereview.member.entity.Member;
import com.codereview.reviewer.entity.Reviewer;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewId;

  @ManyToOne
  private Reviewer reviewer;

  @ManyToOne
  private Member member;

  @Enumerated(EnumType.STRING)
  private Approve approve = Approve.YET;

  @Column(columnDefinition = "TEXT")
  private String contents;
}
