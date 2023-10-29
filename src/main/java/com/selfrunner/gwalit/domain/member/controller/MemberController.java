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
@RequestMapping("")
@Tag(name = "Member", description = "사용자 정보 변경 관련")
public class MemberController {

    private final MemberService memberService;

    @GetMapping({"/member/profile", "/api/v{version}/member/profile"})
    public ApplicationResponse<MemberRes> getProfile(@PathVariable(name = "version", required = false) Long version, @Auth Member member) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, memberService.getProfile(member));
    }

    @PutMapping({"/member/profile", "/api/v{version}/member/profile"})
    public ApplicationResponse<MemberRes> updateProfile(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @Valid @RequestBody PutMemberReq putMemberReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, memberService.updateProfile(member, putMemberReq));
    }

    @PutMapping({"/member/password", "/api/v{version}/member/password"})
    public ApplicationResponse<Void> updatePassword(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @Valid @RequestBody PutPasswordReq putPasswordReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, memberService.updatePassword(member, putPasswordReq));
    }
}
