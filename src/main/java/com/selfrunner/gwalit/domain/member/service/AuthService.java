package com.selfrunner.gwalit.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.selfrunner.gwalit.domain.homework.repository.HomeworkRepository;
import com.selfrunner.gwalit.domain.lecture.repository.LectureRepository;
import com.selfrunner.gwalit.domain.lesson.repository.LessonRepository;
import com.selfrunner.gwalit.domain.member.dto.request.PostAuthCodeReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostAuthPhoneReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostLoginReq;
import com.selfrunner.gwalit.domain.member.dto.request.PostMemberReq;
import com.selfrunner.gwalit.domain.member.dto.response.GetRefreshRes;
import com.selfrunner.gwalit.domain.member.dto.response.PostLoginRes;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberState;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
import com.selfrunner.gwalit.domain.member.repository.MemberAndLectureRepository;
import com.selfrunner.gwalit.domain.member.exception.MemberException;
import com.selfrunner.gwalit.domain.member.repository.MemberRepository;
import com.selfrunner.gwalit.domain.task.repository.TaskRepository;
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
    private final MemberAndLectureRepository memberAndLectureRepository;
    private final LectureRepository lectureRepository;
    private final LessonRepository lessonRepository;
    private final HomeworkRepository homeworkRepository;
    private final TaskRepository taskRepository;

    public Void sendAuthorizationCode(PostAuthPhoneReq postAuthPhoneReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        // Business Logic
        String authorizationCode = smsClient.sendAuthorizationCode(postAuthPhoneReq);

        redisClient.setValue(postAuthPhoneReq.getPhone(), authorizationCode, Long.valueOf(300));

        // Response
        return null;
    }
    public Void checkAuthorizationCode(PostAuthCodeReq postAuthCodeReq) {
        // Business Logic
        boolean result = redisClient.getValue(postAuthCodeReq.getPhone()).equals(postAuthCodeReq.getAuthorizationCode()) ? true : false;

        if(!result) {
            throw new MemberException(ErrorCode.WRONG_AUTHENTICATION_CODE);
        }

        // Response
        return null;
    }

    @Transactional
    public Void sendTemporaryPassword(PostAuthCodeReq postAuthCodeReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        // Validation
        if(!redisClient.getValue(postAuthCodeReq.getPhone()).equals(postAuthCodeReq.getAuthorizationCode())) {
            throw new MemberException(ErrorCode.WRONG_AUTHENTICATION_CODE);
        }
        Member member = memberRepository.findActiveByPhoneAndType(postAuthCodeReq.getPhone(), MemberType.valueOf(postAuthCodeReq.getType())).orElse(null);
        if(member == null) {
            if(MemberType.valueOf(postAuthCodeReq.getType()).equals(MemberType.TEACHER)) {
                if(memberRepository.findActiveByPhoneAndType(postAuthCodeReq.getPhone(), MemberType.STUDENT).orElse(null) != null) {
                    throw new MemberException(ErrorCode.WRONG_TYPE);
                }
            }
            else {
                if(memberRepository.findActiveByPhoneAndType(postAuthCodeReq.getPhone(), MemberType.TEACHER).orElse(null) != null) {
                    throw new MemberException(ErrorCode.WRONG_TYPE);
                }
            }
            throw new MemberException(ErrorCode.NOT_EXIST_PHONE);
        }

        // Business Logic
        String temporaryPassword = smsClient.sendTemporaryPassword(postAuthCodeReq);
        member.encryptPassword(temporaryPassword);
        member.setNeedNotification();

        // Response
        return null;
    }

    @Transactional
    public Void register(PostMemberReq postMemberReq) {
        // Validation: 전화번호와 타입 및 논리적 삭제 여부로 회원가입 이미 진행했는지 여부 확인
        if(memberRepository.findActiveByPhoneAndType(postMemberReq.getPhone(), MemberType.valueOf(postMemberReq.getType())).orElse(null) != null) {
            throw new MemberException(ErrorCode.ALREADY_EXIST_MEMBER);
        }
        // 비밀번호와 비밀번호 확인 일치 여부 확인
        if(!postMemberReq.getPassword().equals(postMemberReq.getPasswordCheck())) {
            throw new MemberException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }

        // Business Logic: 비밀번호 암호화 및 회원 정보 저장
        Member inviter = memberRepository.findInviteByPhoneAndTypeAndState(postMemberReq.getPhone(), MemberType.valueOf(postMemberReq.getType())).orElse(null);
        if(inviter == null) {
            Member member = postMemberReq.toEntity();
            member.encryptPassword(member.getPassword());
            memberRepository.save(member);
        }
        if(inviter != null) {
            inviter.update(postMemberReq);
        }

        // Response
        return null;
    }

    @Transactional
    public PostLoginRes login(PostLoginReq postLoginReq) {
        // Validation: 계정 존재 여부 및 회원탈퇴 여부 확인
        Member member = memberRepository.findActiveByPhoneAndType(postLoginReq.getPhone(), MemberType.valueOf(postLoginReq.getType())).orElse(null);
        if(member == null) {
            throw new MemberException(ErrorCode.NOT_FOUND_EXCEPTION);
        }
        if(!member.getPassword().equals(SHA256.encrypt(postLoginReq.getPassword()))) {
            throw new MemberException(ErrorCode.WRONG_PASSWORD);
        }

        // Business Logic: 토큰 발급 및 Redis 저장
        TokenDto tokenDto = tokenProvider.generateToken(member);
        String key = member.getType() + member.getPhone(); // unique 확인은 phone + type이므로 이를 string으로 저장, 앞 7자리는 type으로 고정
        redisClient.setValue(key, tokenDto.getRefreshToken(), 30 * 24 * 60 * 60 * 1000L);

        // Response
        PostLoginRes postLoginRes = new PostLoginRes().toDto(tokenDto, member);

        return postLoginRes;
    }

    public Void logout(String atk, Member member) {
        // Business Logic
        String key = member.getType() + member.getPhone();
        redisClient.deleteValue(key);
        redisClient.setValue(atk, "logout", tokenProvider.getExpiration(atk));

        // Response
        return null;
    }

    @Transactional
    public GetRefreshRes reissue(HttpServletRequest httpServletRequest) {
        // Validation: RTK 조회
        String rtk = httpServletRequest.getHeader("Authorization");
        String key = tokenProvider.getType(rtk) + tokenProvider.getPhone(rtk);
        String value = redisClient.getValue(key);
        if(rtk.isBlank() || value == null || !value.equals(rtk)) {
            throw new ApplicationException(ErrorCode.WRONG_TOKEN);
        }
        Member member = memberRepository.findActiveByPhoneAndType(tokenProvider.getPhone(rtk), MemberType.valueOf(tokenProvider.getType(rtk))).orElse(null);
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
    public Void withdrawal(Member member) {
        // Validation: 기 탈퇴 여부 확인
        if(member.getDeletedAt() != null) {
            throw new MemberException(ErrorCode.ALREADY_DELETE_MEMBER);
        }

        // Business Logic: Soft Delete
        memberRepository.delete(member);
        List<Long> lectureIdList = memberAndLectureRepository.findLectureIdByMember(member).orElse(null);
        memberAndLectureRepository.deleteMemberAndLecturesByMember(member);
        if(lectureIdList != null && member.getType().equals(MemberType.TEACHER)) {
            taskRepository.deleteAllByLectureIdList(lectureIdList);
            List<Long> lessonIdList = lessonRepository.findAllLessonIdByLectureIdList(lectureIdList);
            homeworkRepository.deleteAllByLessonIdList(lessonIdList);
            lessonRepository.deleteAllByLectureLectureIdList(lectureIdList);
            lectureRepository.deleteAllByLectureIdList(lectureIdList);
        }

        // Response
        return null;
    }
}
