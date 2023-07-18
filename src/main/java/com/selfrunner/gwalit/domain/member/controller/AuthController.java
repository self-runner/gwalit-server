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
    public ApplicationResponse<String> sendAuthorizationCode(@Valid @RequestBody PostAuthPhoneReq postAuthPhoneReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, authService.sendAuthorizationCode(postAuthPhoneReq));
    }

    @Operation(summary = "인증번호 확인 요청")
    @PostMapping("/authorization")
    public ApplicationResponse<Void> checkAuthorizationCode(@Valid @RequestBody PostAuthCodeReq postauthCodeReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, authService.checkAuthorizationCode(postauthCodeReq));
    }

    @Operation(summary = "임시 비밀번호 발급")
    @PostMapping("/password")
    public ApplicationResponse<String> sendTemporaryPassword(@Valid @RequestBody PostAuthCodeReq postAuthCodeReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, authService.sendTemporaryPassword(postAuthCodeReq));
    }

    @Operation(summary = "일반 회원가입")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<String> register(@Valid @RequestBody PostMemberReq postMemberReq) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, authService.register(postMemberReq));
    }

    @Operation(summary = "일반 로그인")
    @PostMapping("/login")
    public ApplicationResponse<PostLoginRes> login(@Valid @RequestBody PostLoginReq postLoginReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, authService.login(postLoginReq));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ApplicationResponse<String> logout(HttpServletRequest httpServletRequest, @Auth Member member) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, authService.logout(httpServletRequest.getHeader("Authorization"), member));
    }

    @Operation(summary = "토큰 재발급")
    @GetMapping("/reissue")
    public ApplicationResponse<GetRefreshRes> reissue(HttpServletRequest httpServletRequest) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, authService.reissue(httpServletRequest));
    }

    @Operation(summary = "회원탈퇴")
    @PostMapping("/withdrawal")
    public ApplicationResponse<String> withdrawal(@Auth Member member) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, authService.withdrawal(member));
    }
}
