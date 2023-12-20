package com.selfrunner.gwalit.util;

import com.selfrunner.gwalit.domain.member.entity.Member;

public class MemberTestUtil {

    public static Member getMockMember() {

        return Member.builder().build();
    }
}
