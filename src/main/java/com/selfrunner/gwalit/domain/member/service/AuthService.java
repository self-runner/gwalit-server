package com.selfrunner.gwalit.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.selfrunner.gwalit.domain.member.dto.request.PostAuthCodeReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostAuthPhoneReq;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.redis.RedisClient;
import com.selfrunner.gwalit.global.util.sms.SmsClient;
import com.selfrunner.gwalit.global.util.sms.dto.SmsMessageDto;
import com.selfrunner.gwalit.global.util.sms.dto.SmsNaverReq;
import com.selfrunner.gwalit.global.util.sms.dto.SmsNaverRes;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final SmsClient smsClient;
    private final RedisClient redisClient;

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
}
