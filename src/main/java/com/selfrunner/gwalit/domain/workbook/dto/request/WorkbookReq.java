package com.selfrunner.gwalit.domain.workbook.dto.request;

import com.selfrunner.gwalit.domain.workbook.entity.Views;
import com.selfrunner.gwalit.domain.workbook.entity.Workbook;
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

    private String explain;

    private String source;

    private Boolean copyright;

    private Boolean isFile;

    // 문제집 파일을 직접 올릴 경우
    public Workbook toFileEntity(Views views, String thumbnailUrl, String workbookFileUrl, String answerFileUrl) {
        return Workbook.builder()
                .views(views)
                .title(this.title)
                .type(this.type)
                .subject(this.subject)
                .subjectDetail(this.subjectDetail)
                .chapter(this.chapter)
                .thumbnailUrl(thumbnailUrl)
                .workbookFileUrl(workbookFileUrl)
                .answerFileUrl(answerFileUrl)
                .problemCount(this.problemCount)
                .time(this.time)
                .explain(this.explain)
                .source(this.source)
                .copyright(this.copyright)
                .isFile(this.isFile)
                .build();
    }

    // 문제를 별도로 등록 후 참조 테이블 엮어서 만드는 경우
    public Workbook toNonFileEntity(Views views, String thumbnailUrl) {
        return Workbook.builder()
                .views(views)
                .title(this.title)
                .type(this.type)
                .subject(this.subject)
                .subjectDetail(this.subjectDetail)
                .chapter(this.chapter)
                .thumbnailUrl(thumbnailUrl)
                .problemCount(this.problemCount)
                .time(this.time)
                .explain(this.explain)
                .source(this.source)
                .copyright(this.copyright)
                .isFile(this.isFile)
                .build();
    }
}
