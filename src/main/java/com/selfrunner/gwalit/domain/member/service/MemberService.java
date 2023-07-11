package com.selfrunner.gwalit.domain.member.service;

import com.selfrunner.gwalit.domain.member.dto.request.PutMemberReq;
import com.selfrunner.gwalit.domain.member.dto.request.PutPasswordReq;
import com.selfrunner.gwalit.domain.member.dto.response.GetMemberRes;
import com.selfrunner.gwalit.domain.member.dto.response.PutMemberRes;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.repository.MemberRepository;
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
            throw new RuntimeException("이미 삭제된 계정입니다");
        }
        if(!member.getMemberId().equals(putMemberReq.getMemberId())) {
            throw new RuntimeException("요청자가 수정 정보 권한을 가지고 있지 않습니다");
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
        Member change = memberRepository.findById(putPasswordReq.getMemberId()).orElseThrow();
        if(change.getDeletedAt() != null) {
            throw new RuntimeException("이미 삭제된 계정입니다");
        }
        if(!member.getMemberId().equals(putPasswordReq.getMemberId())) {
            throw new RuntimeException("요청자가 수정 정보 권한을 가지고 있지 않습니다");
        }

        // Business Logic
        change.encryptPassword(putPasswordReq.getPassword());

        // Response
        return null;
    }
}
