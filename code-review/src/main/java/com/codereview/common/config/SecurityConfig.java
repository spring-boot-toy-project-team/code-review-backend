package com.codereview.common.config;

import com.codereview.security.CustomUserDetailsService;
import com.codereview.security.RestAuthenticationEntryPoint;
import com.codereview.security.jwt.JwtAccessDeniedHandler;
import com.codereview.security.jwt.JwtAuthenticationEntryPoint;
import com.codereview.security.jwt.JwtAuthenticationFilter;
import com.codereview.security.oauth.CustomOAuth2UserService;
import com.codereview.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.codereview.security.oauth.info.OAuth2AuthenticationFailureHandler;
import com.codereview.security.oauth.info.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
  securedEnabled = true,
  jsr250Enabled = true,
  prePostEnabled = true
)
public class SecurityConfig{
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; //인증정보 없을때 401
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler; //접근 권한 없을때 403
  private final CustomOAuth2UserService customOAuth2UserService;
  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
  private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
  private final CustomUserDetailsService customUserDetailsService;
  private final CorsFilter corsFilter;

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
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

    http.cors()
            .and()
        .apply(new CustomDsl())
            .and()
        .csrf().disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
        .headers().frameOptions().disable()
            .and()
        .formLogin().disable()
        .httpBasic().disable()
        .exceptionHandling()
        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
            .and()
        .authorizeRequests()
        .antMatchers("/auth/**", "/oauth2/**")
        .permitAll()
        .antMatchers("/api/**")
        .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
        .anyRequest()
        .permitAll()
            .and()
        .authenticationManager(authenticationManager)
        .oauth2Login()
        .authorizationEndpoint()
        .baseUri("/login/oauth2/authorization")
        .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository())
            .and()
        .redirectionEndpoint()
        .baseUri("/login/oauth2/code/**")
            .and()
        .userInfoEndpoint()
        .userService(customOAuth2UserService)
            .and()
        .successHandler(oAuth2AuthenticationSuccessHandler)
        .failureHandler(oAuth2AuthenticationFailureHandler)
            .and();


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
              .addFilter(corsFilter)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
  }
}
