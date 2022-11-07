package com.codereview.calendar.entity;

import com.codereview.member.entity.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Calendar {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long calendarId;

  @Column(columnDefinition = "TEXT")
  private String contents;

  @ManyToOne
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  @Builder
  public Calendar(Long calendarId, String contents, Member member) {
    this.calendarId = calendarId;
    this.contents = contents;
    this.member = member;
  }
}
