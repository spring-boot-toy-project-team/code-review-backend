package com.codereview.security.oauth;

import com.codereview.common.exception.OAuth2AuthenticationProcessingException;
import com.codereview.member.entity.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOauth2UserInfo(String registrationId, Map<String , Object> attributes){
        if (registrationId.equalsIgnoreCase(AuthProvider.google.toString())){
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.kakao.toString())) {
            return new KakaoOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.github.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        }else
            throw new OAuth2AuthenticationProcessingException("Unsupported Login Type : " + registrationId);
    }
}
