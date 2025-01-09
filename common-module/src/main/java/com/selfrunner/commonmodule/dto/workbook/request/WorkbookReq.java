package com.selfrunner.commonmodule.dto.workbook.request;

import lombok.Getter;

@Getter
public class WorkbookReq {

    private String title;

    private String type;

    private String subject;

    private String subjectDetail;

    private String chapter;

    private Integer problemCount;

    private Integer time;

    private String explanation;

    private String provider;

    private Boolean copyright;

    private Boolean isFile;
}
