package com.codereview.controller;


import com.codereview.board.controller.BoardController;
import com.codereview.board.dto.board.BoardRequestDto;
import com.codereview.board.dto.board.BoardResponseDto;
import com.codereview.board.entity.Board;
import com.codereview.board.entity.BoardTag;
import com.codereview.board.mapper.BoardMapper;
import com.codereview.board.service.BoardService;
import com.codereview.helper.RestPage;
import com.codereview.helper.WithMockCustomUser;
import com.codereview.security.jwt.JwtTokenProvider;
import com.codereview.stub.BoardStubData;
import com.codereview.stub.BoardTagStubData;
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

@WebMvcTest(BoardController.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class BoardControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BoardService boardService;

  @MockBean
  private BoardMapper mapper;

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private Gson gson;


//
//  /**
//   * 게시판 검색
//   */
//

  @Test
  @DisplayName("게시판 조회 테스트")
  public void getBoards() throws Exception {
    // given
    int page = 1, size = 10;
    RestPage<Board> boardRestPage = BoardStubData.BoardRestPage(page - 1, size);
    List<Board> boardList = boardRestPage.getContent();
    List<BoardResponseDto.BoardDto> boardDtoList = BoardStubData.BoardListToBoardInfoDtoList(boardList);


    given(boardService.getBoards(Mockito.anyInt(), Mockito.anyInt())).willReturn(boardRestPage);
    given(mapper.boardListToBoardDtoList(Mockito.anyList())).willReturn(boardDtoList);

    // when
    ResultActions actions = mockMvc.perform(
      get("/boards?page={page}&size={size}", page, size)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
    );

    // then
    actions
      .andExpect(status().isOk())
      .andDo(
        document(
          "board-list-get",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          requestParameters(
            parameterWithName("page").description("페이지 수"),
            parameterWithName("size").description("페이지 크기")
          ),
          responseFields(
            List.of(
              fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
              fieldWithPath("data[].boardId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
              fieldWithPath("data[].title").type(JsonFieldType.STRING).description("게시글 제목"),
              fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("게시글 생성일자"),
              fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING).description("게시글 변경일자"),
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
  @DisplayName("게시글 조회")
  public void getBoard() throws Exception {
    // given
    long boardId = 1L;
    List<BoardTag> boardTagList = BoardTagStubData.boardTagList();
    Board board = BoardStubData.board(boardId, "내용", "제목", boardTagList);
    BoardResponseDto.BoardInfoDto boardInfoDto = BoardStubData.BoardToBoardInfoDto(board);

    given(boardService.getBoard(Mockito.anyLong())).willReturn(board);
    given(mapper.boardToBoardInfoDto(Mockito.any(Board.class))).willReturn(boardInfoDto);

    // when
    ResultActions actions = mockMvc.perform(
      get("/boards/{board-id}", boardId)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    actions
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.message").value("SUCCESS"))
      .andExpect(jsonPath("$.data.boardId").value(boardInfoDto.getBoardId()))
      .andExpect(jsonPath("$.data.title").value(boardInfoDto.getTitle()))
      .andExpect(jsonPath("$.data.contents").value(boardInfoDto.getContents()))
      .andDo(
        document(
          "board-get",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          pathParameters(
            parameterWithName("board-id").description("게시글 식별자")
          ),
          responseFields(
            List.of(
              fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
              fieldWithPath("data.boardId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
              fieldWithPath("data.title").type(JsonFieldType.STRING).description("게시글 제목"),
              fieldWithPath("data.contents").type(JsonFieldType.STRING).description("게시글 내용"),
              fieldWithPath("data.tag[]").type(JsonFieldType.ARRAY).description("게시글 태그"),
              fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("게시글 생성일자"),
              fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("게시글 변경일자"),
              fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
            )
          )
        )
      );
  }

  @Test
  @DisplayName("게시글 등록 테스트")
  @WithMockCustomUser
  public void createBoard() throws Exception {
    // given
    BoardRequestDto.CreateBoardDto createBoardDto = BoardStubData.CreateBoardDto();
    Board board = BoardStubData.CreateBoardDtoToBoardDto(createBoardDto);
    BoardResponseDto.BoardInfoDto boardInfoDto = BoardStubData.BoardToBoardInfoDto(board);
    String content = gson.toJson(createBoardDto);

    given(mapper.createBoardDtoToBoard(Mockito.any(BoardRequestDto.CreateBoardDto.class))).willReturn(board);
    given(boardService.createBoard(Mockito.any(Board.class))).willReturn(board);
    given(mapper.boardToBoardInfoDto(Mockito.any(Board.class))).willReturn(boardInfoDto);

    // when
    ResultActions actions = mockMvc.perform(
      post("/boards")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .content(content)
    );

    // then
    actions
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.message").value("SUCCESS"))
      .andExpect(jsonPath("$.data.boardId").value(boardInfoDto.getBoardId()))
      .andExpect(jsonPath("$.data.title").value(boardInfoDto.getTitle()))
      .andExpect(jsonPath("$.data.contents").value(boardInfoDto.getContents()))
      .andDo(
        document(
          "board-create",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          requestHeaders(headerWithName("Authorization").description("Bearer AccessToken")),
          requestFields(
            List.of(
              fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
              fieldWithPath("contents").type(JsonFieldType.STRING).description("게시글 내용"),
              fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자").ignored(),
              fieldWithPath("tagList").type(JsonFieldType.ARRAY).description("태그")
            )
          ),
          responseFields(
            List.of(
              fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
              fieldWithPath("data.boardId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
              fieldWithPath("data.title").type(JsonFieldType.STRING).description("게시글 제목"),
              fieldWithPath("data.contents").type(JsonFieldType.STRING).description("게시글 내용"),
              fieldWithPath("data.tag[]").type(JsonFieldType.ARRAY).description("게시글 태그"),
              fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("게시글 생성일자"),
              fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("게시글 변경일자"),
              fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
            )
          )
        )
      );
  }

  @Test
  @DisplayName("게시글 변경 테스트")
  @WithMockCustomUser
  public void updateBoardTest() throws Exception {
    // given
    long boardId = 1L;
    BoardRequestDto.UpdateBoardDto updateBoardDto = BoardStubData.UpdateBoardDto(boardId);
    Board board = BoardStubData.UpdateBoardDtoToBoard(updateBoardDto);
    BoardResponseDto.BoardInfoDto boardInfoDto = BoardStubData.BoardToBoardInfoDto(board);
    String content = gson.toJson(updateBoardDto);

    given(mapper.updateBoardToBoard(Mockito.any(BoardRequestDto.UpdateBoardDto.class))).willReturn(new Board());
    given(boardService.updateBoard(Mockito.any(Board.class))).willReturn(board);
    given(mapper.boardToBoardInfoDto(Mockito.any(Board.class))).willReturn(boardInfoDto);

    // when
    ResultActions actions = mockMvc.perform(
      patch("/boards/{board-id}", boardId)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .content(content)
    );

    // then
    actions
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.message").value("SUCCESS"))
      .andExpect(jsonPath("$.data.boardId").value(boardInfoDto.getBoardId()))
      .andExpect(jsonPath("$.data.title").value(boardInfoDto.getTitle()))
      .andExpect(jsonPath("$.data.contents").value(boardInfoDto.getContents()))
      .andDo(
        document(
          "board-update",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          requestHeaders(headerWithName("Authorization").description("Bearer AccessToken")),
          pathParameters(
            parameterWithName("board-id").description("게시글 식별자")
          ),
          requestFields(
            List.of(
              fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 식별자").ignored(),
              fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
              fieldWithPath("contents").type(JsonFieldType.STRING).description("게시글 내용"),
              fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자").ignored(),
              fieldWithPath("tagList").type(JsonFieldType.ARRAY).description("태그")
            )
          ),
          responseFields(
            List.of(
              fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
              fieldWithPath("data.boardId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
              fieldWithPath("data.title").type(JsonFieldType.STRING).description("게시글 제목"),
              fieldWithPath("data.contents").type(JsonFieldType.STRING).description("게시글 내용"),
              fieldWithPath("data.tag[]").type(JsonFieldType.ARRAY).description("게시글 태그"),
              fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("게시글 생성일자"),
              fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("게시글 변경일자"),
              fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
            )
          )
        )
      );
  }

  @Test
  @DisplayName("게시글 삭제 테스트")
  @WithMockCustomUser
  public void deleteBoardTest() throws Exception {
    // given
    long boardId = 1L;
    doNothing().when(boardService).deleteBoardByIdAndMemberId(Mockito.anyLong(), Mockito.anyLong());

    // when
    ResultActions actions = mockMvc.perform(
      delete("/boards/{board-id}", boardId)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
    );

    // then
    actions
      .andExpect(status().isNoContent())
      .andDo(
        document(
          "board-delete",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          requestHeaders(headerWithName("Authorization").description("Bearer AccessToken")),
          pathParameters(
            parameterWithName("board-id").description("게시글 식별자")
          )
        )
      );
  }

  @Test
  @DisplayName("내 게시글 조회")
  @WithMockCustomUser
  public void getMyBoards() throws Exception {
    // given
    int page = 1, size = 10;
    RestPage<Board> boardRestPage = BoardStubData.BoardRestPage(page - 1, size);
    List<Board> boardList = boardRestPage.getContent();
    List<BoardResponseDto.BoardDto> boardDtoList = BoardStubData.BoardListToBoardInfoDtoList(boardList);

    given(boardService.getMyBoards(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt())).willReturn(boardRestPage);
    given(mapper.boardListToBoardDtoList(Mockito.anyList())).willReturn(boardDtoList);

    // when
    ResultActions actions = mockMvc.perform(
      get("/boards/me?page={page}&size={size}", page, size)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
    );

    // then
    actions
      .andExpect(status().isOk())
      .andDo(
        document(
          "board-list-get",
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
              fieldWithPath("data[].boardId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
              fieldWithPath("data[].title").type(JsonFieldType.STRING).description("게시글 제목"),
              fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("게시글 생성일자"),
              fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING).description("게시글 변경일자"),
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
