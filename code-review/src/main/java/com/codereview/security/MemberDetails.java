package com.codereview.security;

import com.codereview.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class MemberDetails implements UserDetails, Serializable {
  private Long memberId;
  private String nickName;
  private String email;
  private List<String> roleList;
  private Map<String, Object> attributes;

  public MemberDetails(Long memberId, String nickName, String email, List<String> roleList) {
    this.memberId = memberId;
    this.nickName = nickName;
    this.email = email;
    this.roleList = roleList;
  }

  public static MemberDetails create(Member member){
    return new MemberDetails(member.getMemberId(), member.getNickName(), member.getEmail(), member.getRoleList());
  }

  public Long getMemberId() {
    return memberId;
  }

  public String getNickName() {
    return nickName;
  }

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    roleList.forEach(n -> {
      authorities.add(() -> n);
    });
    return authorities;
  }

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Override
  public String getPassword() {
    return null;
  }

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Override
  public String getUsername() {
    return this.email;
  }

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Override
  public boolean isEnabled() {
    return true;
  }
}
