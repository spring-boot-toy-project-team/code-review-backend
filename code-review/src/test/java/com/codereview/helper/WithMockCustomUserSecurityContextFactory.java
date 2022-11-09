package com.project.QR.helper;


import com.project.QR.member.entity.AuthProvider;
import com.project.QR.member.entity.Member;
import com.project.QR.security.MemberDetails;
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
    MemberDetails principal = MemberDetails.create(member);
    Authentication auth =
      new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    context.setAuthentication(auth);
    return context;
  }

  private Member getMember(WithMockCustomUser customUser) {
    Member member = new Member();
    member.setMemberId(customUser.memberId());
    member.setEmail(customUser.email());
    member.setPassword(customUser.password());
    member.setName(customUser.name());
    member.setProvider(AuthProvider.valueOf(customUser.provider()));
    member.setRole(customUser.role());
    return member;
  }
}
