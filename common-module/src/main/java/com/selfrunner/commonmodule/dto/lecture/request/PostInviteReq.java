package com.selfrunner.commonmodule.dto.lecture.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PostInviteReq {

    @NotNull(message = "전화번호가 Null입니다.")
    private String phone;
}
