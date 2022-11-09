package com.codereview.controller;

import com.codereview.auth.controller.AuthController;
import com.codereview.auth.service.AuthService;
import com.codereview.member.dto.MemberRequestDto;
import com.codereview.member.entity.Member;
import com.codereview.member.mapper.MemberMapper;
import com.codereview.member.service.MemberService;
import com.codereview.security.jwt.JwtTokenProvider;
import com.codereview.stub.MemberStubData;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;
import java.util.List;

import static com.codereview.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.codereview.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class AuthControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private Gson gson;

  @MockBean
  private MemberService memberService;

  @MockBean
  private AuthService authService;

  @MockBean
  private MemberMapper mapper;

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @Test
  @DisplayName("회원가입 테스트")
  void signUpTest() throws Exception {
    // given
    Member member = MemberStubData.member();
    MemberRequestDto.SingUpDto singUpDto = MemberStubData.singUpDto();
    String content = gson.toJson(singUpDto);
    given(mapper.signUpDtoToMember(Mockito.any(MemberRequestDto.SingUpDto.class))).willReturn(new Member());
    given(memberService.createMember(Mockito.any(Member.class))).willReturn(member);

    // when
    ResultActions actions = mockMvc.perform(
      post("/auth/signup")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(content)
    );

    // then
    actions
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.message").value("WELCOME"))
      .andDo(
        document(
          "signup",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          requestFields(
            List.of(
              fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
              fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
              fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임")
            )
          ),
          responseFields(
            List.of(
              fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
            )
          )
        )
      );
  }

  @Test
  @DisplayName("로그인 테스트")
  void loginTest() throws Exception {
    // given
    MemberRequestDto.LoginDto loginDto = MemberStubData.loginDto();
    String content = gson.toJson(loginDto);

    given(mapper.loginDtoToMember(Mockito.any(MemberRequestDto.LoginDto.class))).willReturn(new Member());
    doNothing().when(authService).login(Mockito.any(Member.class));

    // when
    ResultActions actions = mockMvc.perform(
      post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(content)
    );

    // then
    actions
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.message").value("WELCOME"))
      .andDo(
        document(
          "login",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          requestFields(
            List.of(
              fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
              fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
            )
          ),
          responseFields(
            List.of(
              fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
            )
          )
        )
      );
  }

  @Test
  @DisplayName("토큰 재발급")
  void reIssue() throws Exception {
    // given
    String header = "Bearer accessToken";
    Cookie cookie = new Cookie("refreshToken", "refreshToken");
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    doNothing().when(authService).reIssue(Mockito.any(String.class), Mockito.any(Cookie.class));

    // when
    ResultActions actions = mockMvc.perform(
      get("/auth/reissue")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", header)
        .cookie(cookie)
    );

    // then
    actions
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.message").value("SUCCESS"))
      .andDo(
        document(
          "reIssue",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          requestHeaders(headerWithName("Authorization").description("Bearer AccessToken")),
          responseFields(
            List.of(
              fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
            )
          )
        )
      );
  }
}
