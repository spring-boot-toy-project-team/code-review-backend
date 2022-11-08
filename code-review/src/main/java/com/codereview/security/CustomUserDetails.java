package com.codereview.security;

import com.codereview.member.entity.Member;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
public class CustomUserDetails implements UserDetails, OAuth2User {

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


  public Long getId() {
    return this.member.getMemberId();
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return member.getRoleList().stream().map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return this.member.getNickName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String getName() {
    return this.member.getNickName();
  }

  public String getRole(){
    return String.join(",", member.getRoles());
  }

  public Member getMember() {
    return Member.builder()
            .email(this.member.getEmail())
            .memberId(this.member.getMemberId())
            .nickName(this.member.getNickName())
            .roles(getRole())
            .build();
  }

}
