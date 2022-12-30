package com.codereview.enrollment.entity;

import com.codereview.common.audit.Auditable;
import com.codereview.member.entity.Member;
import com.codereview.reviewer.entity.Reviewer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long enrollmentId;

  @OneToMany
  private Reviewer reviewer;

  @OneToMany
  private Member member;

  private Enroll enroll;

  @Column(columnDefinition = "TEXT")
  private String contents;
}
