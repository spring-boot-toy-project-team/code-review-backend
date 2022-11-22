package com.codereview.sms.entity;

import com.codereview.member.entity.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Sms {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long smsId;

  private String phone;

  private String smsCode;

  private LocalDateTime sendAt = LocalDateTime.now();

  @ManyToOne
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  @Builder
  public Sms(Long smsId, String phone, String smsCode) {
    this.smsId = smsId;
    this.phone = phone;
    this.smsCode = smsCode;
  }
}
