package com.selfrunner.gwalit.domain.member.service;

import com.selfrunner.gwalit.domain.member.dto.request.PutMemberReq;
import com.selfrunner.gwalit.domain.member.dto.request.PutPasswordReq;
import com.selfrunner.gwalit.domain.member.dto.request.TokenReq;
import com.selfrunner.gwalit.domain.member.dto.response.MemberRes;
import com.selfrunner.gwalit.domain.member.dto.response.TokenRes;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.repository.MemberRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.SHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberRes getProfile(Member member) {
        // Business Logic & Response
        return new MemberRes().toDto(member);
    }

    @Transactional
    public MemberRes updateProfile(Member member, PutMemberReq putMemberReq) {
        // Validation
        Member change = memberRepository.findById(putMemberReq.getMemberId()).orElseThrow();
        if(change.getDeletedAt() != null) {
            throw new ApplicationException(ErrorCode.ALREADY_DELETE_MEMBER);
        }
        if(!member.getMemberId().equals(putMemberReq.getMemberId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic
        change.update(putMemberReq);

        // Response
        return new MemberRes().toDto(change);
    }

    @Transactional
    public void updatePassword(Member member, PutPasswordReq putPasswordReq) {
        // Validation
        Member change = memberRepository.findById(putPasswordReq.getMemberId()).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        if(change.getDeletedAt() != null) {
            throw new ApplicationException(ErrorCode.ALREADY_DELETE_MEMBER);
        }
        if(!member.getMemberId().equals(putPasswordReq.getMemberId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        if(!Objects.equals(SHA256.encrypt(putPasswordReq.getOldPassword()), change.getPassword())) {
            throw new ApplicationException(ErrorCode.WRONG_PASSWORD);
        }
        if(!putPasswordReq.getNewPassword().equals(putPasswordReq.getNewPasswordCheck())) {
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }

        // Business Logic
        change.encryptPassword(putPasswordReq.getNewPassword());

        // Response
    }

    @Transactional
    public TokenRes saveToken(Member member, TokenReq tokenReq) {
        // Validation

        // Business Logic
        member.updateToken(tokenReq.getToken());
        Member saveMember = memberRepository.save(member);

        // Response
        return new TokenRes(saveMember.getMemberId(), saveMember.getToken());
    }
}
