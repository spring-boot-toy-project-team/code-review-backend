package com.codereview.sms.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Sms {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long smsId;

  private String phone;

  private String code;

  private LocalDateTime sendAt = LocalDateTime.now();

  @Builder
  public Sms(Long smsId, String phone, String code) {
    this.smsId = smsId;
    this.phone = phone;
    this.code = code;
  }
}
