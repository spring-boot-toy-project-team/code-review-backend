package com.codereview.controller;

import com.codereview.board.entity.Board;
import com.codereview.comment.controller.CommentController;
import com.codereview.comment.dto.CommentResponseDto;
import com.codereview.comment.entity.Comment;
import com.codereview.comment.mapper.CommentMapper;
import com.codereview.comment.service.CommentService;
import com.codereview.common.helper.RestPage;
import com.codereview.helper.WithMockCustomUser;
import com.codereview.security.jwt.JwtTokenProvider;
import com.codereview.stub.BoardStubData;
import com.codereview.stub.CommentStubData;
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
@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class CommentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CommentService commentService;

  @MockBean
  private CommentMapper mapper;

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private Gson gson;

  @Test
  @DisplayName("댓글 조회 테스트")
  public void getComments() throws Exception {
    //given
    int page =1, size = 10;
    Board board = BoardStubData.board();
    RestPage<Comment> commentRestPage = CommentStubData.CommentRestPage(page - 1, size);
    List<Comment> commentList = commentRestPage.getContent();
    List<CommentResponseDto.CommentInfoDto> commentInfoDtoList = CommentStubData.CommentListToCommentInfoList(commentList);

    given(commentService.getComments(Mockito.anyLong(),Mockito.any(Pageable.class))).willReturn(commentRestPage);
    given(mapper.commentListToCommentDtoList(Mockito.anyList())).willReturn(commentInfoDtoList);

    //when
    ResultActions actions = mockMvc.perform(
            get("/board/{board-id}/comments?page={page}&size={size}",board.getBoardId(),page,size)
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON)
    );

    //then
    actions
      .andExpect(status().isOk())
      .andDo(
         document(
           "comment-list-get",
           getRequestPreProcessor(),
           getResponsePreProcessor(),
           pathParameters(
             parameterWithName("board-id").description("게시글 식별자")
           ),
           requestParameters(
             parameterWithName("page").description("페이지 수"),
             parameterWithName("size").description("페이지 크기")
           ),
           responseFields(
             List.of(
               fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
               fieldWithPath("data[].commentId").type(JsonFieldType.NUMBER).description("댓글 식별자"),
               fieldWithPath("data[].contents").type(JsonFieldType.STRING).description("댓글 내용"),
               fieldWithPath("data[].nickName").type(JsonFieldType.STRING).description("댓글 작성자"),
               fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("댓글 생성일자"),
               fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING).description("댓글 변경일자"),
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
}
