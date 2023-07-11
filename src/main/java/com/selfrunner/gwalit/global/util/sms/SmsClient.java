package com.selfrunner.gwalit.global.util.sms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.selfrunner.gwalit.domain.member.dto.request.PostAuthCodeReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostAuthPhoneReq;
import com.selfrunner.gwalit.global.util.sms.dto.SmsMessageDto;
import com.selfrunner.gwalit.global.util.sms.dto.SmsNaverReq;
import com.selfrunner.gwalit.global.util.sms.dto.SmsNaverRes;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SmsClient {
    @Value("${naver-sens-api-service-id}")
    private String serviceId;

    @Value("${naver-sens-api-access-key}")
    private String accessKey;

    @Value("${naver-sens-api-secret-key}")
    private String secretKey;

    @Value("${naver-sens-api-sender-phone}")
    private String senderPhone;

    public String sendAuthorizationCode(PostAuthPhoneReq postAuthPhoneReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        // 인증 번호 생성
        String authorizationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000);
        Long time = System.currentTimeMillis();

        // API 요청 Header, Body 구성
        List<SmsMessageDto> smsMessageDtoList = new ArrayList<>();

        smsMessageDtoList.add(new SmsMessageDto(postAuthPhoneReq.getPhone(), "[과릿] 인증번호: " + authorizationCode + "\n 인증 번호를 입력해주세요."));
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(new SmsNaverReq("SMS", this.senderPhone, authorizationCode, smsMessageDtoList));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", this.accessKey);
        String sig = makeSignature(time); // 암호화
        headers.set("x-ncp-apigw-signature-v2", sig);

        // Header 포함 전송
        HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        SmsNaverRes smsNaverRes = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+this.serviceId+"/messages"), body, SmsNaverRes.class);

        // 실페 시, Error 던지기
        if(!smsNaverRes.getStatusCode().equals("202")) {
            throw new RuntimeException();
        }

        // 성공 시, authorizationCode 반환
        return authorizationCode;
    }

    public String sendTemporaryPassword(PostAuthCodeReq postAuthCodeReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        // 임시 비밀번호 발급
        char list[] = new char[] {
                '1','2','3','4','5','6','7','8','9','0',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                '!','@','#','$','%','^','&','*','(',')'};

        String temporaryPassword = "";

        for(Integer i = 0; i < 10; i++) {
            Integer idx = (int)(Math.random() * (list.length - 0 + 1)) + 0;
            temporaryPassword += list[idx];
        }
        Long time = System.currentTimeMillis();

        // API 요청 Header, Body 구성
        List<SmsMessageDto> smsMessageDtoList = new ArrayList<>();

        smsMessageDtoList.add(new SmsMessageDto(postAuthCodeReq.getPhone(), "[과릿] 임시비밀번호는 " + temporaryPassword + " 입니다. \n 로그인 이후 비밀번호를 변경해주세요."));
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(new SmsNaverReq("SMS", this.senderPhone, temporaryPassword, smsMessageDtoList));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", this.accessKey);
        String sig = makeSignature(time); // 암호화
        headers.set("x-ncp-apigw-signature-v2", sig);

        // Header 포함 전송
        HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        SmsNaverRes smsNaverRes = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+this.serviceId+"/messages"), body, SmsNaverRes.class);

        // 실페 시, Error 던지기
        if(!smsNaverRes.getStatusCode().equals("202")) {
            throw new RuntimeException();
        }

        // 성공 시, 임시 비밀번호 반환
        return temporaryPassword;
    }

    public String makeSignature(Long time) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        String space = " ";					// one space
        String newLine = "\n";					// new line
        String method = "POST";					// method
        String url = "/sms/v2/services/"+ this.serviceId +"/messages";	// url (include query string)
        String timestamp = time.toString();			// current timestamp (epoch)
        String accessKey = this.accessKey;			// access key id (from portal or Sub Account)
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }
}
