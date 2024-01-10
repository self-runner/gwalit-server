package com.selfrunner.gwalit.util;

import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberAndLecture;

public class MemberAndLectureTestUtil {

    public static MemberAndLecture getMockMemberAndLecture(Member member, Lecture lecture) {

        return MemberAndLecture.builder()
                .member(member)
                .lecture(lecture)
                .build();
    }
}
