package com.selfrunner.commonmodule.vo.lecture;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RuleVo {
    private String rule;

    public RuleVo(String rule) {
        this.rule = rule;
    }
}
