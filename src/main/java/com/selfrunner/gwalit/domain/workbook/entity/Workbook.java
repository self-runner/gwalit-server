package com.selfrunner.gwalit.domain.workbook.entity;

import lombok.AccessLevel;
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

    @Column(name = "explain", columnDefinition = "text")
    private String explain;
}
