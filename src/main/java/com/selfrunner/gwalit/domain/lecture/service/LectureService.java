package com.selfrunner.gwalit.domain.lecture.service;


import com.selfrunner.gwalit.domain.lecture.dto.PostLectureReq;
import com.selfrunner.gwalit.domain.lecture.repository.LectureRepository;
import com.selfrunner.gwalit.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;

    @Transactional
    public Void register(Member member, PostLectureReq postLectureReq) {
        // Valid

        // Business Logic

        // Response
        return null;
    }
}
