package com.selfrunner.gwalit.global.util.sms;

import com.selfrunner.gwalit.domain.lecture.dto.request.PostInviteReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostAuthCodeReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostAuthPhoneReq;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Component
public class CoolSMSClient {

    @Value("${coolsms.api-key}")
    private String apiKey;
    @Value("${coolsms.api-secret}")
    private String apiSecretKey;
    @Value("${coolsms.sender-phone}")
    private String senderPhone;

    // 단일 메시지 발송 예제
    public String sendAuthorizationCode(PostAuthPhoneReq postAuthPhoneReq) {
        // 인증 번호 생성
        String authorizationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000);

        Message coolsms = new Message(apiKey, apiSecretKey);

        HashMap<String, String> params = new HashMap<>();
        params.put("to", postAuthPhoneReq.getPhone());
        params.put("from", senderPhone);
        params.put("type", "SMS");
        params.put("text", "[과릿]" + "\n" + "인증번호: " + authorizationCode + "\n" + "인증 번호를 입력해주세요.");
        params.put("app_version", "Gwarit 1.3.4");

        try {
            coolsms.send(params);
        } catch (CoolsmsException e) {
            throw new RuntimeException(e);
        }

        return authorizationCode;
    }

    public String sendTemporaryPassword(PostAuthCodeReq postAuthCodeReq) {
        // 임시 비밀번호 발급
        char[] list = new char[] {
                '1','2','3','4','5','6','7','8','9','0',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                '!','@','#','$','%','^','&','*','(',')'};

        StringBuilder temporaryPassword = new StringBuilder();

        // 숫자 하나 추가
        int idx = (int)(Math.random() * 10); // 0에서 9 사이의 인덱스 선택
        temporaryPassword.append(list[idx]);

        // 문자 하나 추가
        idx = (int)(Math.random() * 26) + 10; // 10에서 35 사이의 인덱스 선택 (알파벳 대문자)
        temporaryPassword.append(list[idx]);

        // 특수 문자 하나 추가
        idx = (int)(Math.random() * 9) + 62; // 36에서 44 사이의 인덱스 선택 (특수 문자)
        temporaryPassword.append(list[idx]);

        for(int i = 3; i < 10; i++) {
            idx = (int) (Math.random() * (list.length));
            temporaryPassword.append(list[idx]);
        }
        // 문자열을 문자 리스트로 변환
        List<Character> charList = new ArrayList<>();
        for (char c : temporaryPassword.toString().toCharArray()) {
            charList.add(c);
        }

        // 문자 리스트를 섞기
        Collections.shuffle(charList);

        // 섞인 문자 리스트를 다시 문자열로 변환
        StringBuilder shuffledPassword = new StringBuilder();
        for (char c : charList) {
            shuffledPassword.append(c);
        }

        Message coolsms = new Message(apiKey, apiSecretKey);

        HashMap<String, String> params = new HashMap<>();
        params.put("to", postAuthCodeReq.getPhone());
        params.put("from", senderPhone);
        params.put("type", "SMS");
        params.put("text", "[과릿]" + "\n" + "임시비밀번호: " + "\n" + temporaryPassword + "\n" + "\n" + "로그인 이후 비밀번호를 변경해주세요.");
        params.put("app_version", "Gwarit 1.3.4");

        try {
            coolsms.send(params);
        } catch (CoolsmsException e) {
            throw new RuntimeException(e);
        }

        return temporaryPassword.toString();
    }

    public void sendInvitation(PostInviteReq postInviteReq, Boolean type) {
        Message coolsms = new Message(apiKey, apiSecretKey);

        HashMap<String, String> params = new HashMap<>();
        params.put("to", postInviteReq.getPhone());
        params.put("from", senderPhone);
        params.put("type", "LMS");
        params.put("app_version", "Gwarit 1.3.4");

        if(type.equals(Boolean.TRUE)) {
            params.put("text", "[과릿]" + "\n" + "선생님으로부터 클래스 초대가 도착했습니다." + "\n" + "아래 링크를 통해 앱 설치 및 회원가입을 통해 수업에 참여해보세요!" + "\n" + "\n" + "안드로이드: " + "https://bit.ly/gwarit-android" + "\n" + "\n" + "애플: " + "https://bit.ly/gwarit-apple");
        }
        if(type.equals(Boolean.FALSE)) {
            params.put("text", "[과릿]" + "\n" + "선생님으로부터 클래스 초대가 도착했습니다." + "\n" + "아래 링크를 클릭 후 앱 열기를 통해 수업에 참여해보세요!" + "\n" + "\n" + "안드로이드: " + "https://bit.ly/gwarit-android" + "\n" + "\n" + "애플: " + "https://bit.ly/gwarit-apple");
        }

        try {
            coolsms.send(params);
        } catch (CoolsmsException e) {
            throw new RuntimeException(e);
        }
    }

}
