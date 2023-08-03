package com.selfrunner.gwalit.domain.lecture.service;


import com.selfrunner.gwalit.domain.lecture.dto.request.PostLectureReq;
import com.selfrunner.gwalit.domain.lecture.dto.request.PutLectureReq;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureMetaRes;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureRes;
import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.lecture.repository.LectureRepository;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberAndLecture;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
import com.selfrunner.gwalit.domain.member.repository.MemberAndLectureRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final MemberAndLectureRepository memberAndLectureRepository;

    @Transactional
    public Void register(Member member, PostLectureReq postLectureReq) {
        // Valid
        if(member.getType() != MemberType.TEACHER) { // 방 생성 권한 없음
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic
        Lecture lecture = postLectureReq.toEntity();
        MemberAndLecture memberAndLecture = MemberAndLecture.builder()
                        .member(member)
                        .lecture(lecture)
                        .build();
        lectureRepository.save(lecture);
        memberAndLectureRepository.save(memberAndLecture);

        // Response
        return null;
    }

    @Transactional
    public Void delete(Member member, Long lectureId) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_CLASS)); // Class 소속 여부 확인

        // Business Logic
        memberAndLectureRepository.delete(memberAndLecture);
        lectureRepository.delete(memberAndLecture.getLecture());

        // Response
        return null;
    }

    public GetLectureRes get(Member member, Long lectureId) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_CLASS)); // Class 소속 여부 확인

        // Business Logic
        GetLectureRes getLectureRes = new GetLectureRes(memberAndLecture.getLecture());

        // Response
        return getLectureRes;
    }

    @Transactional
    public Void update(Member member, Long lectureId, PutLectureReq putLectureReq) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_CLASS)); // Class 소속 여부 확인

        // Business Logic
        memberAndLecture.getLecture().update(putLectureReq);

        // Response
        return null;
    }

    public List<GetLectureMetaRes> getAllMeta(Member member) {
        // Validation

        // Business Logic: member가 해당하는 Class들 조회 -> Class 기본 정보들 다 불러오고, 학생들 정보 역으로 참조해야 함.
        List<GetLectureMetaRes> getLectureMetaResList = lectureRepository.findAllLectureMetaByMember(member).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Response
        return getLectureMetaResList;
    }
}
