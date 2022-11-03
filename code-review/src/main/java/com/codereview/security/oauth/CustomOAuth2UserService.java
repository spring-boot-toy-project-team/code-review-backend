package com.codereview.security.oauth;

import com.codereview.common.exception.OAuth2AuthenticationProcessingException;
import com.codereview.member.entity.AuthProvider;
import com.codereview.member.entity.Member;
import com.codereview.member.repository.MemberRepository;
import com.codereview.security.MemberDetails;
import com.codereview.security.oauth.info.OAuth2UserInfo;
import com.codereview.security.oauth.info.OAuth2UserInfoFactory;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.monitor.os.OsStats;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User){
        AuthProvider authProvider = AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId());
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOauth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes());

        if (oAuth2UserInfo.getEmail().isEmpty())
            throw new OAuth2AuthenticationProcessingException("Empty Email");

        Optional<Member> memberOptional = memberRepository.findByEmail(oAuth2UserInfo.getEmail());
        Member member;
        if (memberOptional.isPresent()){
            member = memberOptional.get();
            if (!authProvider.equals(member.getProvider())) {
                throw new OAuth2AuthenticationProcessingException("Already SignUp Other Provider");
            }
            member = updateMember(member, authProvider);
        }else {
            member = createMember(oAuth2UserInfo, authProvider);
        }
        return MemberDetails.create(member,oAuth2User.getAttributes());
    }

    private Member createMember( OAuth2UserInfo oAuth2UserInfo, AuthProvider authProvider){
        Member member = Member.builder()
                .email(oAuth2UserInfo.getEmail())
                .profileImg(oAuth2UserInfo.getImageUrl())
                .provider(authProvider.name())
                .build();
        return memberRepository.save(member);
    }

    private Member updateMember(Member member, AuthProvider authProvider){
        member.setProvider(authProvider.name());
        member.setEmail(member.getEmail());
        member.setProfileImg(member.getProfileImg());
        return memberRepository.save(member);
    }
}