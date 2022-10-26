package com.codereview.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class Member {
  @Id
  private Long memberId;

  private String email;

  private String nickName;

  @JsonIgnore
  private String password;

  private String role;

  private String provider;

  private String profileImg;

  private String githubUrl;

  private String phone;

  @Builder
  public Member(Long memberId, String email, String nickName, String password, String role,
                   String provider, String profileImg, String githubUrl, String phone) {
    this.memberId = memberId;
    this.email = email;
    this.nickName = nickName;
    this.password = password;
    this.role = role;
    this.provider = provider;
    this.profileImg = profileImg;
    this.githubUrl = githubUrl;
    this.phone = phone;
  }

}
