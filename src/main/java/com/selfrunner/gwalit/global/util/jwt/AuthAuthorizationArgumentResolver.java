package com.selfrunner.gwalit.global.util.jwt;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
import com.selfrunner.gwalit.domain.member.repository.MemberRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.redis.RedisClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;



@Slf4j
@Component
@RequiredArgsConstructor
public class AuthAuthorizationArgumentResolver implements HandlerMethodArgumentResolver {

    private final RedisClient redisClient;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

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
//        if(redisClient.getValue(authorization).equals("logout")) {
//            throw new ApplicationException(ErrorCode.LOGOUT_TOKEN);
//        }
        tokenProvider.validateToken(authorization);

        // 토큰에서 사용자 정보 추출
        String phone = tokenProvider.getPhone(authorization);
        String type = tokenProvider.getType(authorization);

        // 사용자 정보 획득
        Member member = memberRepository.findByPhoneAndType(phone, MemberType.valueOf(type));
        if(member == null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION);
        }

        // 사용자 정보 반환
        return  member;
    }
}