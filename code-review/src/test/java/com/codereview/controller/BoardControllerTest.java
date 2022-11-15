package com.codereview.controller;


import com.codereview.board.controller.BoardController;
import com.codereview.board.dto.board.BoardRequestDto;
import com.codereview.board.dto.board.BoardResponseDto;
import com.codereview.board.entity.Board;
import com.codereview.board.mapper.BoardMapper;
import com.codereview.board.service.BoardService;
import com.codereview.helper.WithMockCustomUser;
import com.codereview.security.jwt.JwtTokenProvider;
import com.codereview.stub.BoardStubData;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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

//  /**
//   * 게시판 리스트 조회
//   */
//  @GetMapping
//  public ResponseEntity<MultiResponseWithPageInfoDto> getBoards(@Positive @PathParam("page") int page,
//                                                                @Positive @PathParam("size") int size) {
//    RestPage<Board> boardRestPage = boardService.getBoards(page -1, size);
//    List<Board> boardList = boardRestPage.getContent();
//    return new ResponseEntity<>(new MultiResponseWithPageInfoDto<>(
//      mapper.boardListToBoardDtoList(boardList),
//      boardRestPage),
//      HttpStatus.OK);
//  }
//
//  /**
//   * 게시판 검색
//   */
//
//  /**
//   * 게시글 조회
//   */
//  @GetMapping("/{board-id}")
//  public ResponseEntity<SingleResponseWithMessageDto> getBoard(@Positive @PathVariable("board-id") long boardId) {
//    Board board = boardService.getBoard(boardId);
//    return new ResponseEntity<>(new SingleResponseWithMessageDto(mapper.boardToBoardInfoDto(board),
//      "SUCCESS"),
//      HttpStatus.OK);
//  }
//

//
//  /**
//   * 게시글 변경
//   */
//  @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
//  @PatchMapping("/{board-id}")
//  public ResponseEntity<SingleResponseWithMessageDto> updateBoard(@AuthenticationPrincipal CustomUserDetails customUserDetails,
//                                                                  @Positive @PathVariable("board-id") long boardId,
//                                                                  @Valid @RequestBody BoardRequestDto.UpdateBoardDto updateBoardDto) {
//    updateBoardDto.setBoardId(boardId);
//    updateBoardDto.setMemberId(customUserDetails.getMember().getMemberId());
//    Board board = boardService.updateBoard(mapper.updateBoardToBoard(updateBoardDto));
//
//    return new ResponseEntity<>(new SingleResponseWithMessageDto(mapper.boardToBoardInfoDto(board),
//      "SUCCESS"),
//      HttpStatus.OK);
//  }
//
//  /**
//   * 게시글 삭제
//   */
//  @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
//  @DeleteMapping("/{board-id}")
//  public ResponseEntity deleteBoard(@AuthenticationPrincipal CustomUserDetails customUserDetails,
//                                    @Positive @PathVariable("board-id") long boardId) {
//    boardService.deleteBoardByIdAndMemberId(boardId, customUserDetails.getMember().getMemberId());
//
//    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//  }
//
//  /**
//   * 내가 쓴 게시글 조회
//   */
//  @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
//  @GetMapping("/me")
//  public ResponseEntity<MultiResponseWithPageInfoDto> getMyBoards(@AuthenticationPrincipal CustomUserDetails customUserDetails,
//                                                                  @Positive @PathParam("page") int page,
//                                                                  @Positive @PathParam("size") int size) {
//    Page<Board> boardRestPage
//      = boardService.getMyBoards(customUserDetails.getMember().getMemberId(), page - 1, size);
//    List<Board> boardList = boardRestPage.getContent();
//
//    return new ResponseEntity(new MultiResponseWithPageInfoDto<>(mapper.boardListToBoardDtoList(boardList),
//      boardRestPage),
//      HttpStatus.OK);
//  }
  @Test
  @DisplayName("게시판 등록 테스트")
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
        .characterEncoding("utf-8")
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .content(content)
    );

    System.out.println(actions.andReturn().getRequest().getContentAsString());

    // then
    actions
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.message").value("SUCCESS"))
      .andExpect(jsonPath("$.data.boardId").value(boardInfoDto.getBoardId()))
      .andDo(
        document(
          "board-create",
          getRequestPreProcessor(),
          getResponsePreProcessor(),
          requestHeaders(headerWithName("Authorization").description("Bearer AccessToken")),
          requestFields(
            List.of(
              fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
              fieldWithPath("contents").type(JsonFieldType.STRING).description("게시글 내용")

            )
          ),
          responseFields(
            List.of(

            )
          )
        )
      );
  }
}
