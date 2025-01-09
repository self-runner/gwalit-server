package com.selfrunner.domainmodule.domain.lecture;

import com.selfrunner.commonmodule.vo.lecture.RuleVo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Rule {

    private final String rule;

    public static Rule convertToRule(RuleVo ruleVo) {
        return new Rule(ruleVo.getRule());
    }

    public static RuleVo converToRuleVo(Rule rule) {
        return new RuleVo(rule.getRule());
    }
}
