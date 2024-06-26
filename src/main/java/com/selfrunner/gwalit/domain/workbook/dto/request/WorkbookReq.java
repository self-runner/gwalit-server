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

    private String explanation;

    private String provider;

    private Boolean copyright;

    private Boolean isFile;

    // 문제집 파일을 직접 올릴 경우
    public Workbook toFileEntity(Views views, String thumbnailUrl, String thumbnailCardUrl, String workbookFileUrl, String answerFileUrl) {
        return Workbook.builder()
                .views(views)
                .title(this.title)
                .type(this.type)
                .subject(this.subject)
                .subjectDetail(this.subjectDetail)
                .chapter(this.chapter)
                .thumbnailUrl(thumbnailUrl)
                .thumbnailCardUrl(thumbnailCardUrl)
                .workbookFileUrl(workbookFileUrl)
                .answerFileUrl(answerFileUrl)
                .problemCount(this.problemCount)
                .time(this.time)
                .explanation(this.explanation)
                .provider(this.provider)
                .copyright(this.copyright)
                .isFile(this.isFile)
                .build();
    }

    // 문제를 별도로 등록 후 참조 테이블 엮어서 만드는 경우
    public Workbook toNonFileEntity(Views views, String thumbnailUrl, String thumbnailCardUrl) {
        return Workbook.builder()
                .views(views)
                .title(this.title)
                .type(this.type)
                .subject(this.subject)
                .subjectDetail(this.subjectDetail)
                .chapter(this.chapter)
                .thumbnailUrl(thumbnailUrl)
                .thumbnailCardUrl(thumbnailCardUrl)
                .problemCount(this.problemCount)
                .time(this.time)
                .explanation(this.explanation)
                .provider(this.provider)
                .copyright(this.copyright)
                .isFile(this.isFile)
                .build();
    }
}
