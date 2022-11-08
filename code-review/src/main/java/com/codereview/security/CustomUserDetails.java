package com.codereview.security;

import com.codereview.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import org.elasticsearch.monitor.os.OsStats;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
public class CustomUserDetails implements UserDetails, OAuth2User {
  @JsonProperty
  private Member member;
  private Map<String, Object> attributes;

  public CustomUserDetails(Member member) {
    this.member = member;
  }

  public static CustomUserDetails create(Member member){
    return new CustomUserDetails(member);
  }

  public static CustomUserDetails create(Member member, Map<String, Object> attributes) {

    CustomUserDetails customUserDetails = new CustomUserDetails(member);
    customUserDetails.setAttributes(attributes);
    return customUserDetails;
  }

  private void setAttributes(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return member.getRoleList().stream().map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
  }

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Override
  public String getPassword() {
    return null;
  }

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Override
  public String getUsername() {
    return this.member.getEmail();
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

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Override
  public String getName() {
    return this.member.getNickName();
  }

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  public String getRoles(){
    return this.member.getRoles();
  }

  public Member getMember() {
    return this.member;
  }
}
