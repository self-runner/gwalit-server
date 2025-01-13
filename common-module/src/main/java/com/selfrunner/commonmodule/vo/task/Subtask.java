package com.selfrunner.commonmodule.vo.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Subtask {

    private String body;

    private Boolean isFinish;
}
