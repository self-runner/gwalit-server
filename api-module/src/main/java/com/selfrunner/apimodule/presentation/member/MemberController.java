package com.selfrunner.apimodule.presentation.member;

import com.selfrunner.apimodule.global.auth.Auth;
import com.selfrunner.commonmodule.common.ApplicationResponse;
import com.selfrunner.commonmodule.dto.member.request.PutMemberReq;
import com.selfrunner.commonmodule.dto.member.request.PutPasswordReq;
import com.selfrunner.commonmodule.dto.member.request.TokenReq;
import com.selfrunner.commonmodule.dto.member.response.MemberRes;
import com.selfrunner.commonmodule.dto.member.response.TokenRes;
import com.selfrunner.commonmodule.exception.ErrorCode;
import com.selfrunner.domainmodule.domain.member.Member;
import com.selfrunner.apimodule.application.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
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
        memberService.updatePassword(member, putPasswordReq);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(description = "FCM 토큰 저장 또는 기존에 있는 토큰 갱신 API")
    @PostMapping("/token")
    public ApplicationResponse<TokenRes> saveToken(@Auth Member member, @Valid @RequestBody TokenReq tokenReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, memberService.saveToken(member, tokenReq));
    }
}
