package com.selfrunner.gwalit.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.selfrunner.gwalit.domain.member.dto.request.PostAuthCodeReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostAuthPhoneReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostMemberReq;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
import com.selfrunner.gwalit.domain.member.repository.MemberRepository;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.redis.RedisClient;
import com.selfrunner.gwalit.global.util.sms.SmsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final SmsClient smsClient;
    private final RedisClient redisClient;
    private final MemberRepository memberRepository;

    public ApplicationResponse<String> sendAuthorizationCode(PostAuthPhoneReq postAuthPhoneReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        // Business Logic
        String authorizationCode = smsClient.sendAuthorizationCode(postAuthPhoneReq);

        redisClient.setValue(postAuthPhoneReq.getPhone(), authorizationCode, Long.valueOf(300));

        // Response
        return ApplicationResponse.create(ErrorCode.SUCCESS);
    }
    public boolean checkAuthorizationCode(PostAuthCodeReq postAuthCodeReq) {
        // Business Logic
        boolean result = redisClient.getValue(postAuthCodeReq.getPhone()).equals(postAuthCodeReq.getAuthorizationCode()) ? true : false;

        // Response
        return result;
    }

    public ApplicationResponse<String> sendTemporaryPassword(PostAuthCodeReq postAuthCodeReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        // Validation
        if(!redisClient.getValue(postAuthCodeReq.getPhone()).equals(postAuthCodeReq.getAuthorizationCode())) {
            throw new RuntimeException();
        }

        // Business Logic
        smsClient.sendTemporaryPassword(postAuthCodeReq);

        // Response
        return ApplicationResponse.ok(ErrorCode.SUCCESS, "임시 비밀번호가 전송되었습니다.");
    }

    public ApplicationResponse<String> register(PostMemberReq postMemberReq) {
        // Validation: 전화번호와 타입으로 회원가입 이미 진행했는지 여부 확인
        if(memberRepository.existsByPhoneAndType(postMemberReq.getPhone(), MemberType.valueOf(postMemberReq.getType()))) {
            throw new RuntimeException();
        }

        // Business Logic: 비밀번호 암호화 및 회원 정보 저장
        Member member = postMemberReq.toEntity();
        member.encryptPassword(member.getPassword());
        memberRepository.save(member);

        // Response
        return ApplicationResponse.create(ErrorCode.SUCCESS, "회원가입을 성공했습니다.");
    }
}
