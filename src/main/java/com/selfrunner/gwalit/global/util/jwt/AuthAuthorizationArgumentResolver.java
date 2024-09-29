package com.selfrunner.gwalit.global.util.jwt;

import com.selfrunner.gwalit.domain.member.entity.Blacklist;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.enumerate.MemberType;
import com.selfrunner.gwalit.domain.member.repository.BlacklistRepository;
import com.selfrunner.gwalit.domain.member.repository.MemberRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.redis.RedisClient;
import com.selfrunner.gwalit.global.util.redis.RedisDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDateTime;


@Slf4j
@Component
@RequiredArgsConstructor
public class AuthAuthorizationArgumentResolver implements HandlerMethodArgumentResolver {

    private final RedisClient redisClient;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final BlacklistRepository blacklistRepository;

    // @Auth 어노테이션 존재 여부 확인
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);
    }

    // @Auth 어노테이션 존재 시, 사용자 정보 확인 후 반환
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader("Authorization");

        // 토큰 정보 유무 확인
        if (authorization == null) {
            throw new ApplicationException(ErrorCode.WRONG_TOKEN);
        }

        // 토큰 유효 여부 확인
        RedisDto redisDto = redisClient.getValue(authorization);
        // Redis 장애 또는 Cache Miss 시, MySQL Data 대체
        if(!redisDto.isSuccess() || redisDto.getValue() == null || !redisDto.getValue().equals("logout")) {
            Blacklist blacklist = blacklistRepository.findBlacklistByToken(authorization).orElse(null);
            // MySQL Data 존재 시, 로그아웃 Value 확인 및 처리
            if(blacklist != null) {
                // ExpiredAt이 현재 시간보다 크면 블랙리스트로 처리
                if(blacklist.getExpiredAt().isAfter(LocalDateTime.now())) {
                    throw new ApplicationException(ErrorCode.LOGOUT_TOKEN);
                }
            // MySQL Data 미존재 시, Exception 던짐
            } else {
                throw new ApplicationException(ErrorCode.WRONG_TOKEN);
            }
        }
        // Redis Data 존재 시, 로그아웃 Value 확인 및 처리
        else {
            throw new ApplicationException(ErrorCode.LOGOUT_TOKEN);
        }

        // 토큰 유효성 검사
        tokenProvider.validateToken(authorization);

        // 토큰에서 사용자 정보 추출
        String phone = tokenProvider.getPhone(authorization);
        String type = tokenProvider.getType(authorization);

        // 사용자 정보 획득
        Member member = memberRepository.findActiveByPhoneAndType(phone, MemberType.valueOf(type)).orElse(null);
        if(member == null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION);
        }

        // 사용자 정보 반환
        return  member;
    }
}