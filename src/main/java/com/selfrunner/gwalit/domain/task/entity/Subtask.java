package com.selfrunner.gwalit.domain.task.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Subtask {

    private String body;

    private Boolean isFinish;
}
