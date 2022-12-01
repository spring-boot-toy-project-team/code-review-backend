package com.codereview.controller;

import com.codereview.member.entity.Member;
import com.codereview.member.repository.MemberRepository;
import com.codereview.security.jwt.JwtTokenProvider;
import com.codereview.sms.controller.SmsController;
import com.codereview.sms.dto.SmsRequestDto;
import com.codereview.sms.entity.Sms;
import com.codereview.sms.mapper.SmsMapper;
import com.codereview.sms.service.SmsService;
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

@WebMvcTest(SmsController.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class SmsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private SmsService smsService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private SmsMapper mapper;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private Member member;


//    @Test
//    @DisplayName("핸드폰번호 인증 요청 테스트")
//    void savePhone() throws Exception {
//        //given
//        SmsRequestDto.SavePhoneDto savePhoneDto = SmsStubData.savePhoneDto();
//        Sms sms = SmsStubData.SavePhoneDtoToSmsDto(savePhoneDto);
//        String content = gson.toJson(savePhoneDto);
//
//        given(mapper.savePhoneToSms(Mockito.any(SmsRequestDto.SavePhoneDto.class))).willReturn(sms);
//        given(smsService.saveMemberPhone(Mockito.any(Sms.class))).willReturn(sms);
//
//        //when
//        ResultActions actions = mockMvc.perform(
//          post("/sms/phone")
//            .accept(MediaType.APPLICATION_JSON)
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Authorization", "Bearer {ACCESS_TOKEN}")
//            .content(content)
//        );
//        //then
//        actions
//          .andExpect(status().isCreated())
//          .andExpect(jsonPath("$.message").value("OK"))
//            .andDo(
//              document(
//                "phone-save",
//                getRequestPreProcessor(),
//                getResponsePreProcessor(),
//                requestHeaders(headerWithName("Authorization").description("Bearer AccessToken")),
//                requestFields(
//                    List.of(
//                        fieldWithPath("phone").type(JsonFieldType.STRING).description("핸드폰 번호")
//                    )
//                ),
//                responseFields(
//                        List.of(
//                            fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
//                        )
//                )
//              )
//            );
//    }
}
