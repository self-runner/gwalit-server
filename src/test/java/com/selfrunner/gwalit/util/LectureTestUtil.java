package com.selfrunner.gwalit.util;

import com.selfrunner.gwalit.domain.lecture.entity.Lecture;

public class LectureTestUtil {

    public static Lecture getMockLecture() {

        return Lecture.builder().build();
    }
}
