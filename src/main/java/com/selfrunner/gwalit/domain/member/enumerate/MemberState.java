package com.selfrunner.gwalit.domain.member.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberState {
    ACTIVE, INVITE, FAKE
}
