package com.selfrunner.gwalit.domain.workbook.entity;

import com.selfrunner.gwalit.domain.workbook.dto.request.WorkbookReq;
import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Workbook")
@SQLDelete(sql = "UPDATE workbook set deleted_at = NOW() where workbook_id = ?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Workbook extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workbook_id", columnDefinition = "bigint")
    private Long workbookId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "views_id", referencedColumnName = "views_id")
    private Views views;

    @Column(name = "title", columnDefinition = "varchar(255)")
    private String title;

    @Column(name = "type", columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private WorkbookType type;

    @Column(name = "subject", columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private Subject subject;

    @Column(name = "subject_detail", columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private SubjectDetail subjectDetail;

    @Column(name = "chapter", columnDefinition = "varchar(255)")
    private String chapter;

    @Column(name = "thumbnail_url", columnDefinition = "text")
    private String thumbnailUrl;

    @Column(name = "workbook_file_url", columnDefinition = "text")
    private String workbookFileUrl;

    @Column(name = "answer_file_url", columnDefinition = "text")
    private String answerFileUrl;

    @Column(name = "problem_count", columnDefinition = "int")
    private Integer problemCount;

    @Column(name = "time", columnDefinition = "int")
    private Integer time;

    @Column(name = "explanation", columnDefinition = "varchar(255)")
    private String explanation;

    @Column(name = "provider", columnDefinition = "varchar(255)")
    private String provider;

    @Column(name = "copyright")
    private Boolean copyright;

    @Column(name = "is_file")
    private Boolean isFile;

    public void update(WorkbookReq workbookReq) {
        this.title = workbookReq.getTitle();
        this.type = WorkbookType.valueOf(workbookReq.getType());
        this.subject = Subject.valueOf(workbookReq.getSubject());
        this.subjectDetail = SubjectDetail.valueOf(workbookReq.getSubjectDetail());
        this.chapter = workbookReq.getChapter();
        this.problemCount = workbookReq.getProblemCount();
        this.time = workbookReq.getTime();
        this.explanation = workbookReq.getExplanation();
        this.provider = workbookReq.getProvider();
        this.copyright = workbookReq.getCopyright();
        this.isFile = workbookReq.getIsFile();
    }

    public void updateThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void updateWorkbookFileUrl(String workbookFileUrl) {
        this.workbookFileUrl = workbookFileUrl;
    }

    public void updateAnswerFileUrl(String answerFileUrl) {
        this.answerFileUrl = answerFileUrl;
    }

    @Builder
    public Workbook(Views views, String title, String type, String subject, String subjectDetail, String chapter, String thumbnailUrl, String workbookFileUrl, String answerFileUrl, Integer problemCount, Integer time, String explanation, String provider, Boolean copyright, Boolean isFile) {
        this.views = views;
        this.title = title;
        this.type = WorkbookType.valueOf(type);
        this.subject = Subject.valueOf(subject);
        this.subjectDetail = SubjectDetail.valueOf(subjectDetail);
        this.chapter = chapter;
        this.thumbnailUrl = thumbnailUrl;
        this.workbookFileUrl = workbookFileUrl;
        this.answerFileUrl = answerFileUrl;
        this.problemCount = problemCount;
        this.time = time;
        this.explanation = explanation;
        this.provider = provider;
        this.copyright = copyright;
        this.isFile = isFile;
    }
}
