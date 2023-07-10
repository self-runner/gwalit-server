package com.selfrunner.gwalit.domain.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.selfrunner.gwalit.domain.member.dto.request.PostAuthCodeReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostAuthPhoneReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostLoginReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostMemberReq;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.service.AuthService;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.jwt.Auth;
import com.selfrunner.gwalit.global.util.jwt.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;
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
    public ApplicationResponse<String> sendAuthorizationCode(@RequestBody PostAuthPhoneReq postAuthPhoneReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        return authService.sendAuthorizationCode(postAuthPhoneReq);
    }

    @Operation(summary = "인증번호 확인 요청")
    @PostMapping("/authorization")
    public ApplicationResponse<String> checkAuthorizationCode(@RequestBody PostAuthCodeReq postauthCodeReq) {
        String result = authService.checkAuthorizationCode(postauthCodeReq) ? "인증번호가 일치합니다." : "인증번호가 일치하지 않습니다";

        return ApplicationResponse.ok(ErrorCode.SUCCESS, result);
    }

    @Operation(summary = "임시 비밀번호 발급")
    @PostMapping("/password")
    public ApplicationResponse<String> sendTemporaryPassword(@RequestBody PostAuthCodeReq postAuthCodeReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        return authService.sendTemporaryPassword(postAuthCodeReq);
    }

    @Operation(summary = "일반 회원가입")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<String> register(@RequestBody PostMemberReq postMemberReq) {
        return authService.register(postMemberReq);
    }

    @Operation(summary = "일반 로그인")
    @PostMapping("/login")
    public ApplicationResponse<TokenDto> singIn(@RequestBody PostLoginReq postLoginReq) {
        return authService.signIn(postLoginReq);
    }
}
