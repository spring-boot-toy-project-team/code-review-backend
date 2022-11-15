package com.codereview.member.mapper;

import com.codereview.member.dto.MemberRequestDto;
import com.codereview.member.dto.MemberResponseDto;
import com.codereview.member.entity.Member;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-11-15T17:16:21+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.13 (Oracle Corporation)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public Member signUpDtoToMember(MemberRequestDto.SingUpDto singUpDto) {
        if ( singUpDto == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.email( singUpDto.getEmail() );
        member.nickName( singUpDto.getNickName() );
        member.password( singUpDto.getPassword() );

        return member.build();
    }

    @Override
    public Member loginDtoToMember(MemberRequestDto.LoginDto loginDto) {
        if ( loginDto == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.email( loginDto.getEmail() );
        member.password( loginDto.getPassword() );

        return member.build();
    }

    @Override
    public MemberResponseDto.MemberInfo memberToMemberInfo(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberResponseDto.MemberInfo.MemberInfoBuilder memberInfo = MemberResponseDto.MemberInfo.builder();

        memberInfo.nickName( member.getNickName() );
        memberInfo.profileImg( member.getProfileImg() );
        memberInfo.githubUrl( member.getGithubUrl() );
        Set<String> set = member.getSkills();
        if ( set != null ) {
            memberInfo.skills( new LinkedHashSet<String>( set ) );
        }

        return memberInfo.build();
    }

    @Override
    public Member updateDtoToMember(MemberRequestDto.UpdateDto updateDto) {
        if ( updateDto == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.memberId( updateDto.getMemberId() );
        member.nickName( updateDto.getNickName() );
        member.password( updateDto.getPassword() );
        member.profileImg( updateDto.getProfileImg() );
        member.githubUrl( updateDto.getGithubUrl() );
        member.phone( updateDto.getPhone() );
        Set<String> set = updateDto.getSkills();
        if ( set != null ) {
            member.skills( new LinkedHashSet<String>( set ) );
        }

        return member.build();
    }

    @Override
    public MemberResponseDto.UpdateDto memberToUpdateDto(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberResponseDto.UpdateDto.UpdateDtoBuilder updateDto = MemberResponseDto.UpdateDto.builder();

        updateDto.nickName( member.getNickName() );
        updateDto.phone( member.getPhone() );
        updateDto.profileImg( member.getProfileImg() );
        updateDto.githubUrl( member.getGithubUrl() );
        updateDto.password( member.getPassword() );
        Set<String> set = member.getSkills();
        if ( set != null ) {
            updateDto.skills( new LinkedHashSet<String>( set ) );
        }

        return updateDto.build();
    }
}
