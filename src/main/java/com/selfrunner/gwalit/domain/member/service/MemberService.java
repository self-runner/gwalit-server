package com.selfrunner.gwalit.domain.member.service;

import com.selfrunner.gwalit.domain.member.dto.response.GetMemberRes;
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
}
