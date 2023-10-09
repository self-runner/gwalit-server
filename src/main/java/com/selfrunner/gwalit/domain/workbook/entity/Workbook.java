package com.selfrunner.gwalit.domain.workbook.entity;

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
public class Workbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workbook_id", columnDefinition = "bigint")
    private Long workbookId;


    @Column(name = "title", columnDefinition = "varchar(255)")
    private String title;

    @Column(name = "type", columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private WorkbookType type;

    @Column(name = "thumbnail_link", columnDefinition = "text")
    private String thumbnailLink;

    @Column(name = "workbook_file_link", columnDefinition = "text")
    private String workbookFileLink;

    @Column(name = "answer_file_link", columnDefinition = "text")
    private String answerFileLink;

    @Column(name = "time", columnDefinition = "int")
    private Integer time;

    @Column(name = "explain", columnDefinition = "varchar(255)")
    private String explain;

    @Builder
    public Workbook(String title, String type, String thumbnailLink, String workbookFileLink, String answerFileLink, Integer time, String explain) {
        this.title = title;
        this.type = WorkbookType.valueOf(type);
        this.thumbnailLink = thumbnailLink;
        this.workbookFileLink = workbookFileLink;
        this.answerFileLink = answerFileLink;
        this.time = time;
        this.explain = explain;
    }
}
