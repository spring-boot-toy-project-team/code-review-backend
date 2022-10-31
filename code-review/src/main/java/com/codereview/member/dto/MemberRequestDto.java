package com.codereview.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

public class MemberRequestDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SingUpDto{
        @NotBlank
        @Email
        @Pattern(regexp = "^[a-zA-Z\\d_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z\\d.-]+$", message = "이메일 형식이 잘못되었습니다")
        private String email;

        @NotBlank(message = "최소 8자리 이상 12자리 이하여야 합니다")
        @Length(min = 8,max = 12)
        private String password;

        @NotBlank
        private String nickName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginDto {
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\\d_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z\\d.-]+$")
        private String email;

        @NotBlank(message = "최소 8자리 이상 12자리 이하여야 합니다")
        @Length(min = 8,max = 12)
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateDto{
        private String password;
        private String nickName;
        private String phone;
        private String profileImg;
        private String githubUrl;
        private Set<String> skills;
        private long memberId;

    }
}