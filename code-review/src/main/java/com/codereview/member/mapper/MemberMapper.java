package com.codereview.member.mapper;


import com.codereview.member.dto.MemberRequestDto;
import com.codereview.member.dto.MemberResponseDto;
import com.codereview.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    Member signUpDtoToMember(MemberRequestDto.SingUpDto singUpDto);

    Member loginDtoToMember(MemberRequestDto.LoginDto loginDto);

    MemberResponseDto.MemberInfoDto memberToMemberInfo(Member member);

    Member updateDtoToMember(MemberRequestDto.UpdateDto updateDto);

    MemberResponseDto.UpdateDto memberToUpdateDto(Member member);
}