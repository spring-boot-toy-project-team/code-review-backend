package com.codereview.notice.entity;

import com.codereview.member.entity.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Notice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long noticeId;

  private String contents;

  @ManyToOne
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  @Builder
  public Notice(Long noticeId, String contents, Member member) {
    this.noticeId = noticeId;
    this.contents = contents;
    this.member = member;
  }
}
