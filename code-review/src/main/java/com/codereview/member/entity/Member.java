package com.codereview.member.entity;

import com.codereview.common.audit.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberId;

  private String email;

  private String nickName;

  @JsonIgnore
  private String password;

  private String roles;

  @Enumerated(value = EnumType.STRING)
  private AuthProvider provider;

  private String profileImg;

  private String githubUrl;

  private String phone;

  @Enumerated(value = EnumType.STRING)
  private Verified emailVerified;

  private String verifiedCode;

  @ElementCollection
  @CollectionTable(joinColumns = @JoinColumn(name = "MEMBER_ID"))
  private Set<String> skills;

  @Builder
  public Member(Long memberId, String email, String nickName, String password, String roles,
                   AuthProvider provider, String profileImg, String githubUrl, String phone, Set<String> skills, Verified emailVerified) {
    this.memberId = memberId;
    this.email = email;
    this.nickName = nickName;
    this.password = password;
    this.roles = roles;
    this.provider = provider;
    this.profileImg = profileImg;
    this.githubUrl = githubUrl;
    this.phone = phone;
    this.skills = skills;
    this.emailVerified = emailVerified;
  }

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  public List<String> getRoleList() {
    if(this.roles.length() > 0)
      return Arrays.asList(this.roles.split(","));
    return new ArrayList<>();
  }
}
