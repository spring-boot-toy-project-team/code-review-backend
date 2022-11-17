package com.codereview.helper;


import com.codereview.member.entity.Member;
import com.codereview.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Member member = getMember(customUser);
    CustomUserDetails principal = CustomUserDetails.create(member);
    Authentication auth =
      new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    context.setAuthentication(auth);
    return context;
  }

  private Member getMember(WithMockCustomUser customUser) {
    return Member.builder()
      .memberId(customUser.memberId())
      .email(customUser.email())
      .password(customUser.password())
      .nickName(customUser.nickName())
      .roles(customUser.roles())
      .provider(customUser.provider())
      .build();
  }
}
