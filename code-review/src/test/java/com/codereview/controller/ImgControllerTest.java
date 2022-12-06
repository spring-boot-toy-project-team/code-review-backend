package com.codereview.controller;

import com.codereview.helper.WithMockCustomUser;
import com.codereview.img.controller.ImgController;
import com.codereview.img.dto.ImgResponseDto;
import com.codereview.img.entity.Img;
import com.codereview.img.entity.ImgType;
import com.codereview.img.mapper.ImgMapper;
import com.codereview.img.service.ImgService;
import com.codereview.security.jwt.JwtTokenProvider;
import com.codereview.stub.ImgStubData;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.codereview.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.codereview.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockCustomUser
@WebMvcTest(ImgController.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class ImgControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ImgService imgService;

  @MockBean
  private ImgMapper mapper;

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @Test
  @DisplayName("게시글 이미지 업로드 컨트롤러 테스트")
  public void uploadBoardImgTest() throws Exception {
    // given
    String type = "board";
    Img img = ImgStubData.img();
    ImgResponseDto.ImgInfo imgInfo = ImgStubData.imgToImgInfo(img);
    MockMultipartFile file = new MockMultipartFile("file", "img.jpg", "image/jpeg",
      "img".getBytes());
    given(imgService.saveImg(Mockito.any(MultipartFile.class), Mockito.any(ImgType.class))).willReturn(img);
    given(mapper.imgToImgInfo(Mockito.any(Img.class))).willReturn(imgInfo);

    // when
    ResultActions actions = mockMvc.perform(
      multipart("/api/v1/images/{type}", type)
        .file(file)
        .accept(MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA)
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
    );

    // then
    actions
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.uri").value(imgInfo.getUri()))
      .andExpect(jsonPath("$.message").value("SUCCESS"))
      .andDo(
        document(
          "board-img-upload",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          requestHeaders(headerWithName("Authorization").description("Bearer AccessToken")),
          pathParameters(
            parameterWithName("type").description("board (게시글)")
          ),
          requestParts(
            partWithName("file").description("이미지 파일")
          ),
          responseFields(
            List.of(
              fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
              fieldWithPath("data.uri").type(JsonFieldType.STRING).description("이미지 URI"),
              fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
            )
          )
        )
      );
  }

  @Test
  @DisplayName("댓글 이미지 업로드 컨트롤러 테스트")
  public void uploadCommentImgTest() throws Exception {
// given
    String type = "comment";
    Img img = ImgStubData.img();
    ImgResponseDto.ImgInfo imgInfo = ImgStubData.imgToImgInfo(img);
    MockMultipartFile file = new MockMultipartFile("file", "img.jpg", "image/jpeg",
      "img".getBytes());
    given(imgService.saveImg(Mockito.any(MultipartFile.class), Mockito.any(ImgType.class))).willReturn(img);
    given(mapper.imgToImgInfo(Mockito.any(Img.class))).willReturn(imgInfo);

    // when
    ResultActions actions = mockMvc.perform(
      multipart("/api/v1/images/{type}", type)
        .file(file)
        .accept(MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA)
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
    );

    // then
    actions
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.uri").value(imgInfo.getUri()))
      .andExpect(jsonPath("$.message").value("SUCCESS"))
      .andDo(
        document(
          "board-img-upload",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          requestHeaders(headerWithName("Authorization").description("Bearer AccessToken")),
          pathParameters(
            parameterWithName("type").description("comment (댓글)")
          ),
          requestParts(
            partWithName("file").description("이미지 파일")
          ),
          responseFields(
            List.of(
              fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
              fieldWithPath("data.uri").type(JsonFieldType.STRING).description("이미지 URI"),
              fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
            )
          )
        )
      );
  }
}
