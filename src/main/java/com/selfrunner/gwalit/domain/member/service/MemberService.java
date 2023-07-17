package com.selfrunner.gwalit.domain.member.service;

import com.selfrunner.gwalit.domain.member.dto.request.PutMemberReq;
import com.selfrunner.gwalit.domain.member.dto.request.PutPasswordReq;
import com.selfrunner.gwalit.domain.member.dto.response.GetMemberRes;
import com.selfrunner.gwalit.domain.member.dto.response.PutMemberRes;
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

    public GetMemberRes getProfile(Member member) {
        // Business Logic
        GetMemberRes getMemberRes = new GetMemberRes().toDto(member);

        // Response
        return getMemberRes;
    }

    @Transactional
    public PutMemberRes updateProfile(Member member, PutMemberReq putMemberReq) {
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
        PutMemberRes putMemberRes = new PutMemberRes().toDto(change);
        return putMemberRes;
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
