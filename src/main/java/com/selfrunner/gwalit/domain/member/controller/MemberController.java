package com.selfrunner.gwalit.domain.member.controller;

import com.selfrunner.gwalit.domain.member.dto.request.PutMemberReq;
import com.selfrunner.gwalit.domain.member.dto.request.PutPasswordReq;
import com.selfrunner.gwalit.domain.member.dto.response.MemberRes;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.service.MemberService;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.jwt.Auth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "Member", description = "사용자 정보 변경 관련")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/profile")
    public ApplicationResponse<MemberRes> getProfile(@Auth Member member) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, memberService.getProfile(member));
    }

    @PutMapping("/profile")
    public ApplicationResponse<MemberRes> updateProfile(@Auth Member member, @Valid @RequestBody PutMemberReq putMemberReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, memberService.updateProfile(member, putMemberReq));
    }

    @PutMapping("/password")
    public ApplicationResponse<Void> updatePassword(@Auth Member member, @Valid @RequestBody PutPasswordReq putPasswordReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, memberService.updatePassword(member, putPasswordReq));
    }
}
