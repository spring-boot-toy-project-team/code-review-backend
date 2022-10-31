package com.codereview.member.service;

import com.codereview.exception.BusinessLogicException;
import com.codereview.exception.ExceptionCode;
import com.codereview.member.dto.MemberResponseDto;
import com.codereview.member.entity.Member;
import com.codereview.member.mapper.MemberMapper;
import com.codereview.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.codereview.member.dto.MemberRequestDto.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper mapper;

    @Transactional(rollbackFor = BusinessLogicException.class)
    public Member createMember(SingUpDto dto) {
        verifyEmail(mapper.signUpDtoToMember(dto).getEmail());
        return memberRepository.save(mapper.signUpDtoToMember(dto));
    }

    public MemberResponseDto.UpdateDto updateMember(UpdateDto member) {

        Member findMember = findVerifiedMember(member.getMemberId());

        Optional.ofNullable(member.getNickName()).ifPresent(findMember::setNickName);
        Optional.ofNullable(member.getPassword()).ifPresent(findMember::setPassword);
        Optional.ofNullable(member.getPhone()).ifPresent(findMember::setPhone);
        Optional.ofNullable(member.getGithubUrl()).ifPresent(findMember::setGithubUrl);
        Optional.ofNullable(member.getProfileImg()).ifPresent(findMember::setProfileImg);
        Optional.ofNullable(member.getSkills()).ifPresent(findMember::setSkills);
        return mapper.memberToUpdateDto(memberRepository.save(findMember));
    }

    public void deleteMember(long memberId) {
        Member member = findVerifiedMember(memberId);
        memberRepository.delete(member);
    }

    public void verifyEmail(String email) {
        memberRepository.findByEmail(email)
                .ifPresent((member) -> new BusinessLogicException(ExceptionCode.MEMBER_ALREADY_EXISTS));
    }

    @Transactional(readOnly = true)
    public Member findVerifiedMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Member findMember(long memberId) {
        return findVerifiedMember(memberId);
    }

    @Transactional(readOnly = true)
    public Member findVerifiedMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }
}
