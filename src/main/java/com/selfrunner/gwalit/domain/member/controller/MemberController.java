package com.selfrunner.gwalit.domain.member.controller;

import com.selfrunner.gwalit.domain.member.dto.request.PutMemberReq;
import com.selfrunner.gwalit.domain.member.dto.response.GetMemberRes;
import com.selfrunner.gwalit.domain.member.dto.response.PutMemberRes;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.service.MemberService;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.jwt.Auth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "Member", description = "사용자 정보 변경 관련")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/profile")
    public ApplicationResponse<GetMemberRes> getProfile(@Auth Member member) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, memberService.getProfile(member));
    }

    @PutMapping("/profile")
    public ApplicationResponse<PutMemberRes> updateProfile(@Auth Member member, @RequestBody PutMemberReq putMemberReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, memberService.updateProfile(member, putMemberReq));
    }
}
