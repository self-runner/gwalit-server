package com.selfrunner.apimodule.application.member;

import com.selfrunner.commonmodule.common.SHA256;
import com.selfrunner.commonmodule.dto.member.request.PutMemberReq;
import com.selfrunner.commonmodule.dto.member.request.PutPasswordReq;
import com.selfrunner.commonmodule.dto.member.request.TokenReq;
import com.selfrunner.commonmodule.dto.member.response.MemberRes;
import com.selfrunner.commonmodule.dto.member.response.TokenRes;
import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;
import com.selfrunner.domainmodule.domain.member.Member;
import com.selfrunner.domainmodule.domain.member.repository.MemberRepository;
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
        return new MemberRes(member.getMemberId(),
                member.getName(),
                member.getType(),
                member.getPhone(),
                member.getSchool(),
                member.getGrade(),
                member.getNeedNotification(),
                member.getIsAdvertisement(),
                member.getIsPrivacy());
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
        Member updateMember = memberRepository.save(change);

        // Response
        return new MemberRes(updateMember.getMemberId(),
                updateMember.getName(),
                updateMember.getType(),
                updateMember.getPhone(),
                updateMember.getSchool(),
                updateMember.getGrade(),
                updateMember.getNeedNotification(),
                updateMember.getIsAdvertisement(),
                updateMember.getIsPrivacy());
    }

    @Transactional
    public void updatePassword(Member member, PutPasswordReq putPasswordReq) {
        // Validation
        Member change = memberRepository.findById(putPasswordReq.getMemberId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
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
