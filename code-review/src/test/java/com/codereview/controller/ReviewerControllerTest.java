package com.codereview.controller;

import com.codereview.common.helper.RestPage;
import com.codereview.helper.WithMockCustomUser;
import com.codereview.member.entity.Member;
import com.codereview.reviewer.controller.ReviewerController;
import com.codereview.reviewer.dto.ReviewerRequestDto.CreateReviewerDto;
import com.codereview.reviewer.dto.ReviewerRequestDto.UpdateReviewerDto;
import com.codereview.reviewer.dto.ReviewerResponseDto.ReviewerInfoDto;
import com.codereview.reviewer.dto.ReviewerResponseDto.ReviewerShortInfoDto;
import com.codereview.reviewer.entity.Reviewer;
import com.codereview.reviewer.mapper.ReviewerMapper;
import com.codereview.reviewer.service.ReviewerService;
import com.codereview.security.jwt.JwtTokenProvider;
import com.codereview.stub.MemberStubData;
import com.codereview.stub.ReviewerStubData;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;

import static com.codereview.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.codereview.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockCustomUser
@WebMvcTest(ReviewerController.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class ReviewerControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ReviewerService reviewerService;

  @MockBean
  private ReviewerMapper mapper;

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private Gson gson;

  @Test
  @DisplayName("리뷰어들 조회 테스트")
  public void getReviewers() throws Exception {
    // given
    int page = 1, size = 10;
    RestPage<Reviewer> reviewerRestPage = ReviewerStubData.reviewerRestPage(page , size);
    List<Reviewer> reviewerList = reviewerRestPage.getContent();
    List<ReviewerShortInfoDto> reviewerShortInfoDtoList
      = ReviewerStubData.reviewerListToReviewerShortInfoDto(reviewerList);

    given(reviewerService.getReviewers(Mockito.any(Pageable.class))).willReturn(reviewerRestPage);
    given(mapper.reviewerListToReviewerShortInfoList(Mockito.anyList())).willReturn(reviewerShortInfoDtoList);

    // when
    ResultActions actions = mockMvc.perform(
      get("/api/v1/reviewers?page={page}&size={size}", page, size)
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
    );

    // then
    actions
      .andExpect(status().isOk())
      .andDo(
        document(
          "reviewer-list-get",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          requestHeaders(headerWithName("Authorization").description("Bearer AccessToken")),
          requestParameters(
            parameterWithName("page").description("페이지 수"),
            parameterWithName("size").description("페이지 크기")
          ),
          responseFields(
            List.of(
              fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
              fieldWithPath("data[].reviewerId").type(JsonFieldType.NUMBER).description("리뷰어 식별자"),
              fieldWithPath("data[].career").type(JsonFieldType.STRING).description("리뷰어 경력 사항"),
              fieldWithPath("data[].position").type(JsonFieldType.STRING).description("리뷰어 포지션"),
              fieldWithPath("data[].skills").type(JsonFieldType.ARRAY).description("리뷰어 기술 스텍"),
              fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
              fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("현재 페이지"),
              fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("현재 페이지 크기"),
              fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 데이터 수"),
              fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
            )
          )
        )
      );
  }

  @Test
  @DisplayName("리뷰어 조회 테스트")
  public void getReviewerTest() throws Exception {
    // given
    Long reviewerId = 1L;
    Member member = MemberStubData.member();
    Reviewer reviewer = ReviewerStubData.reviewer(member);
    ReviewerInfoDto reviewerInfoDto = ReviewerStubData.reviewerToReviewerInfoDto(reviewer);

    given(reviewerService.getReviewer(Mockito.anyLong())).willReturn(reviewer);
    given(mapper.reviewerToReviewerInfoDto(Mockito.any(Reviewer.class))).willReturn(reviewerInfoDto);

    // when
    ResultActions actions = mockMvc.perform(
      get("/api/v1/reviewers/{reviewer-id}", reviewerId)
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    actions
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.reviewerId").value(reviewerInfoDto.getReviewerId()))
      .andExpect(jsonPath("$.data.career").value(reviewerInfoDto.getCareer()))
      .andExpect(jsonPath("$.data.position").value(reviewerInfoDto.getPosition()))
      .andExpect(jsonPath("$.data.introduction").value(reviewerInfoDto.getIntroduction()))
      .andExpect(jsonPath("$.message").value("SUCCESS"))
      .andDo(
        document(
          "reviewer-get",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          requestHeaders(headerWithName("Authorization").description("Bearer AccessToken")),
          pathParameters(
            parameterWithName("reviewer-id").description("리뷰어 식별자")
          ),
          responseFields(
            List.of(
              fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
              fieldWithPath("data.reviewerId").type(JsonFieldType.NUMBER).description("리뷰어 식별자"),
              fieldWithPath("data.career").type(JsonFieldType.STRING).description("리뷰어 경력 사항"),
              fieldWithPath("data.position").type(JsonFieldType.STRING).description("리뷰어 포지션"),
              fieldWithPath("data.skills").type(JsonFieldType.ARRAY).description("리뷰어 기술 스텍"),
              fieldWithPath("data.introduction").type(JsonFieldType.STRING).description("리뷰어 소개글"),
              fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
            )
          )
        )
      );
  }

  @Test
  @DisplayName("리뷰어 등록 테스트")
  public void enrollReviewerTest() throws Exception {
    // given
    Member member = MemberStubData.member();
    CreateReviewerDto createReviewerDto = ReviewerStubData.createReviewerDto(member);
    Reviewer reviewer = ReviewerStubData.reviewer(member);
    ReviewerInfoDto reviewerInfoDto = ReviewerStubData.reviewerToReviewerInfoDto(reviewer);
    String content = gson.toJson(createReviewerDto);

    given(mapper.createReviewerDtoToReviewer(Mockito.any(CreateReviewerDto.class))).willReturn(new Reviewer());
    given(reviewerService.enroll(Mockito.any(Reviewer.class))).willReturn(reviewer);
    given(mapper.reviewerToReviewerInfoDto(Mockito.any(Reviewer.class))).willReturn(reviewerInfoDto);

    // when
    ResultActions actions = mockMvc.perform(
      post("/api/v1/reviewers")
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content)
    );

    // then
    actions
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.data.reviewerId").value(reviewerInfoDto.getReviewerId()))
      .andExpect(jsonPath("$.data.career").value(reviewerInfoDto.getCareer()))
      .andExpect(jsonPath("$.data.position").value(reviewerInfoDto.getPosition()))
      .andExpect(jsonPath("$.data.introduction").value(reviewerInfoDto.getIntroduction()))
      .andExpect(jsonPath("$.message").value("SUCCESS"))
      .andDo(
        document(
          "reviewer-enroll",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          requestHeaders(headerWithName("Authorization").description("Bearer AccessToken")),
          requestFields(
            List.of(
              fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자").ignored(),
              fieldWithPath("skills").type(JsonFieldType.ARRAY).description("리뷰어 기술 스텍"),
              fieldWithPath("introduction").type(JsonFieldType.STRING).description("리뷰어 소개글"),
              fieldWithPath("position").type(JsonFieldType.ARRAY).description("리뷰어 포지션"),
              fieldWithPath("career").type(JsonFieldType.STRING).description("리뷰어 경력 사항")
            )
          ),
          responseFields(
            List.of(
              fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
              fieldWithPath("data.reviewerId").type(JsonFieldType.NUMBER).description("리뷰어 식별자"),
              fieldWithPath("data.career").type(JsonFieldType.STRING).description("리뷰어 경력 사항"),
              fieldWithPath("data.position").type(JsonFieldType.STRING).description("리뷰어 포지션"),
              fieldWithPath("data.skills").type(JsonFieldType.ARRAY).description("리뷰어 기술 스텍"),
              fieldWithPath("data.introduction").type(JsonFieldType.STRING).description("리뷰어 소개글"),
              fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
            )
          )
        )
      );
  }

  @Test
  @DisplayName("리뷰어 업데이트 테스트")
  public void updateReviewerTest() throws Exception {
    // given
    Long reviewerId = 1L;
    Member member = MemberStubData.member();
    Reviewer reviewer = ReviewerStubData.updatedReviewer(member);
    UpdateReviewerDto updateReviewerDto = ReviewerStubData.updateReviewerDto();
    ReviewerInfoDto reviewerInfoDto = ReviewerStubData.reviewerToReviewerInfoDto(reviewer);
    String content = gson.toJson(updateReviewerDto);

    given(mapper.updateReviewerDtoToReviewer(Mockito.any(UpdateReviewerDto.class))).willReturn(new Reviewer());
    given(reviewerService.updateReviewer(Mockito.any(Reviewer.class))).willReturn(reviewer);
    given(mapper.reviewerToReviewerInfoDto(Mockito.any(Reviewer.class))).willReturn(reviewerInfoDto);

    // when
    ResultActions actions = mockMvc.perform(
      patch("/api/v1/reviewers/{reviewer-id}", reviewerId)
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content)
    );

    // then
    actions
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.reviewerId").value(reviewerInfoDto.getReviewerId()))
      .andExpect(jsonPath("$.data.career").value(reviewerInfoDto.getCareer()))
      .andExpect(jsonPath("$.data.position").value(reviewerInfoDto.getPosition()))
      .andExpect(jsonPath("$.data.introduction").value(reviewerInfoDto.getIntroduction()))
      .andExpect(jsonPath("$.message").value("SUCCESS"))
      .andDo(
        document(
          "reviewer-update",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          requestHeaders(headerWithName("Authorization").description("Bearer AccessToken")),
          pathParameters(
            parameterWithName("reviewer-id").description("리뷰어 식별자")
          ),
          requestFields(
            List.of(
              fieldWithPath("reviewerId").type(JsonFieldType.NUMBER).description("리뷰어 식별자").ignored(),
              fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자").ignored(),
              fieldWithPath("skills").type(JsonFieldType.ARRAY).description("리뷰어 기술 스텍"),
              fieldWithPath("introduction").type(JsonFieldType.STRING).description("리뷰어 소개글"),
              fieldWithPath("position").type(JsonFieldType.ARRAY).description("리뷰어 포지션"),
              fieldWithPath("career").type(JsonFieldType.STRING).description("리뷰어 경력 사항")
            )
          ),
          responseFields(
            List.of(
              fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
              fieldWithPath("data.reviewerId").type(JsonFieldType.NUMBER).description("리뷰어 식별자"),
              fieldWithPath("data.career").type(JsonFieldType.STRING).description("리뷰어 경력 사항"),
              fieldWithPath("data.position").type(JsonFieldType.STRING).description("리뷰어 포지션"),
              fieldWithPath("data.skills").type(JsonFieldType.ARRAY).description("리뷰어 기술 스텍"),
              fieldWithPath("data.introduction").type(JsonFieldType.STRING).description("리뷰어 소개글"),
              fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
            )
          )
        )
      );
  }

  @Test
  @DisplayName("리뷰어 삭제 테스트")
  public void deleteReviewerTest() throws Exception {
    // given
    Long reviewerId = 1L;
    doNothing().when(reviewerService).deleteReviewer(Mockito.anyLong(), Mockito.anyLong());

    // when
    ResultActions actions = mockMvc.perform(
      delete("/api/v1/reviewers/{reviewer-id}", reviewerId)
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    actions
      .andExpect(status().isNoContent())
      .andDo(
        document(
          "reviewer-delete",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          requestHeaders(headerWithName("Authorization").description("Bearer AccessToken")),
          pathParameters(
            parameterWithName("reviewer-id").description("리뷰어 식별자")
          )
        )
      );
  }

  @Test
  @DisplayName("리뷰어 경력사항 조회 테스트")
  public void getCareersTest() throws Exception {
    // given
    Map<String, String> careers = ReviewerStubData.careers();
    given(reviewerService.getCareers()).willReturn(careers);

    // when
    ResultActions actions = mockMvc.perform(
      get("/api/v1/reviewers/career")
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    actions
      .andExpect(status().isOk())
      .andDo(
        document(
          "careers-get",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          requestHeaders(headerWithName("Authorization").description("Bearer AccessToken")),
          responseFields(
            List.of(
              fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
              fieldWithPath("data.JUNIOR").type(JsonFieldType.STRING).description("0 ~ 3년차"),
              fieldWithPath("data.MID_LEVEL").type(JsonFieldType.STRING).description("3 ~ 6년차"),
              fieldWithPath("data.SENIOR").type(JsonFieldType.STRING).description("6년차 이상"),
              fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
            )
          )
        )
      );
  }

  @Test
  @DisplayName("리뷰어 포지션 조회 테스트")
  public void getPositionsTest() throws Exception {
    // given
    Map<String, String> positions = ReviewerStubData.positions();
    given(reviewerService.getPositions()).willReturn(positions);

    // when
    ResultActions actions = mockMvc.perform(
      get("/api/v1/reviewers/position")
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    actions
      .andExpect(status().isOk())
      .andDo(
        document(
          "careers-get",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          requestHeaders(headerWithName("Authorization").description("Bearer AccessToken")),
          responseFields(
            List.of(
              fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
              fieldWithPath("data.DBA").type(JsonFieldType.STRING).description("DBA"),
              fieldWithPath("data.DEVOPS").type(JsonFieldType.STRING).description("DevOps"),
              fieldWithPath("data.GAME").type(JsonFieldType.STRING).description("게임 개발"),
              fieldWithPath("data.BACK_END").type(JsonFieldType.STRING).description("백엔드 개발"),
              fieldWithPath("data.SECURE").type(JsonFieldType.STRING).description("보안 담당"),
              fieldWithPath("data.FRONT_END").type(JsonFieldType.STRING).description("프론트 개발"),
              fieldWithPath("data.FULL_STACK").type(JsonFieldType.STRING).description("풀스택 개발"),
              fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
            )
          )
        )
      );
  }
}
