package com.codereview.member.entity;

import com.codereview.common.audit.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Member extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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


  @ElementCollection
  @CollectionTable(joinColumns = @JoinColumn(name = "MEMBER_ID"))
  private Set<String> skills;

  @Builder
  public Member(Long memberId, String email, String nickName, String password, String role,
                   String provider, String profileImg, String githubUrl, String phone, Set<String> skills) {
    this.memberId = memberId;
    this.email = email;
    this.nickName = nickName;
    this.password = password;
    this.role = role;
    this.provider = provider;
    this.profileImg = profileImg;
    this.githubUrl = githubUrl;
    this.phone = phone;
    this.skills = skills;
  }
}
