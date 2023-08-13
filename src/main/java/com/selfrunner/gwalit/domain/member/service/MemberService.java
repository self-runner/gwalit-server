package com.selfrunner.gwalit.domain.member.service;

import com.selfrunner.gwalit.domain.member.dto.request.PutMemberReq;
import com.selfrunner.gwalit.domain.member.dto.request.PutPasswordReq;
import com.selfrunner.gwalit.domain.member.dto.response.MemberRes;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.repository.MemberRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberRes getProfile(Member member) {
        // Business Logic
        MemberRes memberRes = new MemberRes().toDto(member);

        // Response
        return memberRes;
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
        MemberRes memberRes = new MemberRes().toDto(change);
        return memberRes;
    }

    @Transactional
    public Void updatePassword(Member member, PutPasswordReq putPasswordReq) {
        // Validation
        Member change = memberRepository.findById(putPasswordReq.getMemberId()).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        if(change.getDeletedAt() != null) {
            throw new ApplicationException(ErrorCode.ALREADY_DELETE_MEMBER);
        }
        if(!member.getMemberId().equals(putPasswordReq.getMemberId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic
        change.encryptPassword(putPasswordReq.getPassword());

        // Response
        return null;
    }
}
