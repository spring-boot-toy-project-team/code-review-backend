package com.codereview.security;

import com.codereview.member.entity.Member;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
public class CustomUserDetails implements UserDetails, OAuth2User {
  private long memberId;
  private String nickName;
  private String email;
  private List<String> roles;
  private Map<String, Object> attributes;

  public CustomUserDetails(long memberId, String nickName, String email, List<String> roleList) {
    this.memberId = memberId;
    this.nickName = nickName;
    this.email = email;
    this.roles = roleList;
  }

  public static CustomUserDetails create(Member member){
    return new CustomUserDetails(member.getMemberId(), member.getNickName(), member.getEmail(), member.getRoleList());
  }

  public static CustomUserDetails create(Member member, Map<String, Object> attributes) {

    CustomUserDetails customUserDetails = new CustomUserDetails(member.getMemberId(), member.getNickName(),member.getEmail(), member.getRoleList());
    customUserDetails.setAttributes(attributes);
    return customUserDetails;
  }

  private void setAttributes(Map<String, Object> attributes) {
    this.attributes = attributes;
  }


  public Long getId() {
    return this.memberId;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return this.email;
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
    return this.nickName;
  }

  public String getRole(){
    return String.join(",", roles);
  }

  public Member getMember() {
    return Member.builder()
            .email(this.email)
            .memberId(this.memberId)
            .nickName(this.nickName)
            .roles(getRole())
            .build();
  }
}
