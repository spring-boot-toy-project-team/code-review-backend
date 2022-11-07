package com.codereview.security.oauth.info;

import com.codereview.common.exception.OAuth2AuthenticationProcessingException;
import com.codereview.member.entity.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOauth2UserInfo(AuthProvider authProvider, Map<String , Object> attributes){
        if (authProvider.equals(AuthProvider.google)){
            return new GoogleOAuth2UserInfo(attributes);
        } else if (authProvider.equals(AuthProvider.kakao)) {
            return new KakaoOAuth2UserInfo(attributes);
        } else if (authProvider.equals(AuthProvider.github)) {
            return new GithubOAuth2UserInfo(attributes);
        }else
            throw new OAuth2AuthenticationProcessingException("Unsupported Login Type : " + authProvider.name());
    }
}
