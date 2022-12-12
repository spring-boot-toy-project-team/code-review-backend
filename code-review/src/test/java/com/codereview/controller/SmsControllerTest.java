package com.codereview.controller;

import com.codereview.helper.WithMockCustomUser;
import com.codereview.member.entity.Member;
import com.codereview.security.jwt.JwtTokenProvider;
import com.codereview.sms.controller.SmsController;
import com.codereview.sms.dto.SmsRequestDto;
import com.codereview.sms.entity.Sms;
import com.codereview.sms.mapper.SmsMapper;
import com.codereview.sms.service.SmsService;
import com.codereview.stub.MemberStubData;
import com.codereview.stub.SmsStubData;
import com.codereview.util.RandomStringUtils;
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

@WithMockCustomUser
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
    private SmsMapper mapper;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("핸드폰번호 인증 요청 테스트")
    void savePhone() throws Exception {
        //given
        SmsRequestDto.SavePhoneDto savePhoneDto = SmsStubData.savePhoneDto();
        Sms sms = SmsStubData.SavePhoneDtoToSmsDto(savePhoneDto);
        String content = gson.toJson(savePhoneDto);

        given(mapper.savePhoneToSms(Mockito.any(SmsRequestDto.SavePhoneDto.class))).willReturn(sms);
        given(smsService.saveMemberPhone(Mockito.any(Sms.class))).willReturn(sms);

        //when
        ResultActions actions = mockMvc.perform(
          post("/sms/phone")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .content(content)
        );
        //then
        actions
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.message").value("OK"))
            .andDo(
              document(
                "phone-save",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                requestHeaders(headerWithName("Authorization").description("Bearer AccessToken")),
                requestFields(
                    List.of(
                        fieldWithPath("phone").type(JsonFieldType.STRING).description("핸드폰 번호"),
                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자").ignored()
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
    @DisplayName("핸드폰 인증 성공")
    void phoneValidation() throws Exception {
        //given
        Member member = MemberStubData.member();
        SmsRequestDto.validationPhoneDto validationPhoneDto = SmsStubData.validationPhoneDto();
        String smsCode = RandomStringUtils.generateAuthNo();
        String content = gson.toJson(validationPhoneDto);

        doNothing().when(smsService).verifiedBySmsCode(smsCode,member.getMemberId());
        //when
        ResultActions actions = mockMvc.perform(
            post("/sms/phone-validation")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer {ACCESS_TOKEN}")
                .content(content)
        );
        //then
        actions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("SUCCESS"))
            .andDo(
                document(
                "validation-phone",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                requestHeaders(headerWithName("Authorization").description("Bearer AccessToken")),
                requestFields(
                    List.of(
                        fieldWithPath("smsCode").type(JsonFieldType.STRING).description("인증 코드"),
                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자").ignored()
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
}
