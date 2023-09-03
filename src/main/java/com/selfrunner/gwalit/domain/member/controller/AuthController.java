package com.selfrunner.gwalit.domain.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.selfrunner.gwalit.domain.member.dto.request.PostAuthCodeReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostAuthPhoneReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostLoginReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostMemberReq;
import com.selfrunner.gwalit.domain.member.dto.response.GetRefreshRes;
import com.selfrunner.gwalit.domain.member.dto.response.PostLoginRes;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.service.AuthService;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.jwt.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "토큰, 전화번호 인증 관련")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "인증번호 전송 요청")
    @PostMapping("/phone")
    public ApplicationResponse<Void> sendAuthorizationCode(@Valid @RequestBody PostAuthPhoneReq postAuthPhoneReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        authService.sendAuthorizationCode(postAuthPhoneReq);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(summary = "인증번호 확인 요청")
    @PostMapping("/authorization")
    public ApplicationResponse<Void> checkAuthorizationCode(@Valid @RequestBody PostAuthCodeReq postauthCodeReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, authService.checkAuthorizationCode(postauthCodeReq));
    }

    @Operation(summary = "임시 비밀번호 발급")
    @PostMapping("/password")
    public ApplicationResponse<Void> sendTemporaryPassword(@Valid @RequestBody PostAuthCodeReq postAuthCodeReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        authService.sendTemporaryPassword(postAuthCodeReq);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(summary = "일반 회원가입")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<Void> register(@Valid @RequestBody PostMemberReq postMemberReq) {
        authService.register(postMemberReq);
        return ApplicationResponse.create(ErrorCode.SUCCESS);
    }

    @Operation(summary = "일반 로그인")
    @PostMapping("/login")
    public ApplicationResponse<PostLoginRes> login(@Valid @RequestBody PostLoginReq postLoginReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, authService.login(postLoginReq));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ApplicationResponse<Void> logout(HttpServletRequest httpServletRequest, @Auth Member member) {
        authService.logout(httpServletRequest.getHeader("Authorization"), member);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(summary = "토큰 재발급")
    @GetMapping("/reissue")
    public ApplicationResponse<GetRefreshRes> reissue(HttpServletRequest httpServletRequest) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, authService.reissue(httpServletRequest));
    }

    @Operation(summary = "회원탈퇴")
    @PostMapping("/withdrawal")
    public ApplicationResponse<Void> withdrawal(@Auth Member member) {
        authService.withdrawal(member);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }
}
