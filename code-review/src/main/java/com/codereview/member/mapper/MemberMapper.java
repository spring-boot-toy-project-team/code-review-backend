package main.wheelmaster.member.mapper;

import main.wheelmaster.member.dto.MemberRequestDto;
import main.wheelmaster.member.dto.MemberResponseDto;
import main.wheelmaster.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member signUpDtoToMember(MemberRequestDto.singUpDto singUpDto);

    Member loginDtoToMember(MemberRequestDto.loginDto loginDto);

    MemberResponseDto.MemberInfo memberToMemberInfo(Member member);

    Member updateDtoToMember(MemberRequestDto.updateDto updateDto);

    MemberResponseDto.UpdateDto memberToUpdateDto(Member member);
}