package com.selfrunner.gwalit.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.selfrunner.gwalit.domain.member.dto.request.PostAuthCodeReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostAuthPhoneReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostLoginReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostMemberReq;
import com.selfrunner.gwalit.domain.member.dto.response.GetRefreshRes;
import com.selfrunner.gwalit.domain.member.dto.response.PostLoginRes;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
import com.selfrunner.gwalit.domain.member.repository.MemberRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.SHA256;
import com.selfrunner.gwalit.global.util.jwt.TokenDto;
import com.selfrunner.gwalit.global.util.jwt.TokenProvider;
import com.selfrunner.gwalit.global.util.redis.RedisClient;
import com.selfrunner.gwalit.global.util.sms.SmsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final SmsClient smsClient;
    private final RedisClient redisClient;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public String sendAuthorizationCode(PostAuthPhoneReq postAuthPhoneReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        // Business Logic
        String authorizationCode = smsClient.sendAuthorizationCode(postAuthPhoneReq);

        redisClient.setValue(postAuthPhoneReq.getPhone(), authorizationCode, Long.valueOf(300));

        // Response
        String response = "인증 번호를 전송했습니다.";
        return response;
    }
    public Void checkAuthorizationCode(PostAuthCodeReq postAuthCodeReq) {
        // Business Logic
        boolean result = redisClient.getValue(postAuthCodeReq.getPhone()).equals(postAuthCodeReq.getAuthorizationCode()) ? true : false;

        if(!result) {
            throw new ApplicationException(ErrorCode.WRONG_AUTHENTICATION_CODE);
        }

        // Response
        return null;
    }

    @Transactional
    public String sendTemporaryPassword(PostAuthCodeReq postAuthCodeReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        // Validation
        Member member = memberRepository.findByPhoneAndType(postAuthCodeReq.getPhone(), MemberType.valueOf(postAuthCodeReq.getType()));
        if(member == null) {
            if(MemberType.valueOf(postAuthCodeReq.getType()).equals(MemberType.TEACHER)) {
                if(memberRepository.existsByPhoneAndType(postAuthCodeReq.getPhone(), MemberType.STUDENT)) {
                    throw new ApplicationException(ErrorCode.WRONG_TYPE);
                }
            }
            else {
                if(memberRepository.existsByPhoneAndType(postAuthCodeReq.getPhone(), MemberType.TEACHER)) {
                    throw new ApplicationException(ErrorCode.WRONG_TYPE);
                }
            }
            throw new ApplicationException(ErrorCode.NOT_EXIST_PHONE);
        }
        if(!redisClient.getValue(postAuthCodeReq.getPhone()).equals(postAuthCodeReq.getAuthorizationCode())) {
            throw new RuntimeException("인증번호가 일치하지 않습니다.");
        }

        // Business Logic
        String temporaryPassword = smsClient.sendTemporaryPassword(postAuthCodeReq);
        member.encryptPassword(temporaryPassword);
        member.setNeedNotification();

        // Response
        String response = "임시 비밀번호가 전송되었습니다.";
        return response;
    }

    @Transactional
    public String register(PostMemberReq postMemberReq) {
        // Validation: 전화번호와 타입으로 회원가입 이미 진행했는지 여부 확인
        if(memberRepository.existsByPhoneAndType(postMemberReq.getPhone(), MemberType.valueOf(postMemberReq.getType()))) {
            throw new ApplicationException(ErrorCode.ALREADY_EXIST_MEMBER);
        }

        // Business Logic: 비밀번호 암호화 및 회원 정보 저장
        Member member = postMemberReq.toEntity();
        member.encryptPassword(member.getPassword());
        memberRepository.save(member);

        // Response
        String response = "회원가입을 성공했습니다.";
        return response;
    }

    @Transactional
    public PostLoginRes login(PostLoginReq postLoginReq) {
        // Validation: 계정 존재 여부 및 회원탈퇴 여부 확인
        Member member = memberRepository.findByPhoneAndType(postLoginReq.getPhone(), MemberType.valueOf(postLoginReq.getType()));
        if(member.getDeletedAt() != null) {
            throw new ApplicationException(ErrorCode.ALREADY_DELETE_MEMBER);
        }
        if(!member.getPassword().equals(SHA256.encrypt(postLoginReq.getPassword()))) {
            throw new ApplicationException(ErrorCode.WRONG_PASSWORD);
        }

        // Business Logic: 토큰 발급 및 Redis 저장
        TokenDto tokenDto = tokenProvider.generateToken(member);
        String key = member.getType() + member.getPhone(); // unique 확인은 phone + type이므로 이를 string으로 저장, 앞 7자리는 type으로 고정
        redisClient.setValue(key, tokenDto.getRefreshToken(), 30 * 24 * 60 * 60 * 1000L);

        // Response
        PostLoginRes postLoginRes = new PostLoginRes().toDto(tokenDto, member);

        return postLoginRes;
    }

    public String logout(String atk, Member member) {
        // Business Logic
        String key = member.getType() + member.getPhone();
        redisClient.deleteValue(key);
        redisClient.setValue(atk, "logout", tokenProvider.getExpiration(atk));

        // Response
        String response = "로그아웃이 완료되었습니다";
        return response;
    }

    @Transactional
    public GetRefreshRes reissue(HttpServletRequest httpServletRequest) {
        // Validation: RTK 조회
        String rtk = httpServletRequest.getHeader("Authorization");
        String key = tokenProvider.getType(rtk) + tokenProvider.getPhone(rtk);
        if(rtk.isBlank() || !redisClient.getValue(key).equals(rtk)) {
            throw new ApplicationException(ErrorCode.WRONG_TOKEN);
        }
        Member member = memberRepository.findByPhoneAndType(tokenProvider.getPhone(rtk), MemberType.valueOf(tokenProvider.getType(rtk)));
        if(member == null) {
            throw new ApplicationException(ErrorCode.WRONG_TOKEN);
        }

        // Business Logic
        String atk = tokenProvider.regenerateToken(member);

        // Response
        GetRefreshRes getRefreshRes = new GetRefreshRes().toDto(atk);

        return getRefreshRes;
    }

    @Transactional
    public String withdrawal(Member member) {
        // Validation: 기 탈퇴 여부 확인
        if(member.getDeletedAt() != null) {
            throw new ApplicationException(ErrorCode.ALREADY_DELETE_MEMBER);
        }

        // Business Logic: Soft Delete
        System.out.println("삭제 로직 실행" + member.getMemberId().toString());
        memberRepository.delete(member);

        // Response
        String response = "회원 탈퇴가 완료되었습니다";
        return response;
    }
}
