package com.codereview.helper;

import com.codereview.member.entity.AuthProvider;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
  long memberId() default 1L;
  String email() default "hgd@gmail.com";
  String nickName() default "hgd";
  String password() default "12345678";
  String roles() default "ROLE_USER";
  AuthProvider provider() default AuthProvider.local;
}
