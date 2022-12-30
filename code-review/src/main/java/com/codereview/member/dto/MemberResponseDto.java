package com.codereview.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

public class MemberResponseDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberInfoDto {
        private String nickName;
        private String profileImg;
        private String githubUrl;
        private Set<String> skills;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateDto {
        private String nickName;
        private String phone;
        private String profileImg;
        private String githubUrl;
        private String password;
        private Set<String> skills;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberComments {
        private String nickName;
    }

}