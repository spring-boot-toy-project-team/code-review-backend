package com.codereview.common.config;

import com.codereview.security.RestAuthenticationEntryPoint;
import com.codereview.security.jwt.JwtAccessDeniedHandler;
import com.codereview.security.jwt.JwtAuthenticationEntryPoint;
import com.codereview.security.jwt.JwtAuthenticationFilter;
import com.codereview.security.oauth.CustomOAuth2UserService;
import com.codereview.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; //인증정보 없을때 401
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler; //접근 권한 없을때 403
  private final CustomOAuth2UserService customOAuth2UserService;

  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository(){
    return new HttpCookieOAuth2AuthorizationRequestRepository();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    http.csrf().disable();
    http.headers().frameOptions().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .formLogin().disable()
      .httpBasic().disable()
            .exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint())
            .and()
            .authorizeRequests()
            .antMatchers("/auth/**", "/oauth2/**")
            .access("hasRole()'ROLE_USER','ROLE_ADMIN')")
            .anyRequest()
            .permitAll()
            .and()
            .oauth2Login()
            .authorizationEndpoint().baseUri("/oauth2/authorization")
            .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository())
            .and()
            .redirectionEndpoint().baseUri("/login/oauth2/code/*")
            .and()
            .userInfoEndpoint().userService(customOAuth2UserService)
            .and()
            .successHandler(oauth2AuthenticationSuccessHandler)
            .failureHandler(oAuth2AuthenticationFailureHandler)
            .and()
      .apply(new CustomDsl());
    http.authorizeRequests()
      .antMatchers("/api/**")
        .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
      .anyRequest()
        .permitAll();
    http.exceptionHandling()
      .authenticationEntryPoint(jwtAuthenticationEntryPoint)
      .accessDeniedHandler(jwtAccessDeniedHandler);

    return http.build();
  }

  public class CustomDsl extends AbstractHttpConfigurer<CustomDsl, HttpSecurity> {
    @Override
    public void configure(HttpSecurity builder) throws Exception {
      AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
      builder
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
  }
}
