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
        String authorizationCode = smsClient.sendAuthorizationCode(postAuthPhoneReq);

        // Redis 저장
        redisClient.setValue(postAuthPhoneReq.getPhone(), authorizationCode, Long.valueOf(300));

        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }
    public boolean checkAuthorizationCode(PostAuthCodeReq postAuthCodeReq) {
        boolean result = redisClient.getValue(postAuthCodeReq.getPhone()).equals(postAuthCodeReq.getAuthorizationCode()) ? true : false;

        return result;
    }
}
