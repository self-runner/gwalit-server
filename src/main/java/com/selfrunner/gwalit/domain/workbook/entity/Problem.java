package com.selfrunner.gwalit.domain.workbook.entity;

import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Problem")
@SQLDelete(sql = "UPDATE problem set deleted_at = NOW() where problem_id = ?")
@Where(clause = "deleted_at IS NULL")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id", columnDefinition = "bigint")
    private Long problemId;

    @Column(name = "subject", nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private Subject subject;

    @Column(name = "subject_detail", nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private SubjectDetail subjectDetail;

    @Column(name = "type")
    private ProblemType type;

    @Column(name = "image_url",  columnDefinition = "text")
    private String imageUrl;

    @Column(name = "body", columnDefinition = "varchar(255)")
    private String body;

    @Column(name = "answer", nullable = false, columnDefinition = "int")
    private Integer answer;

    @Column(name = "solve_url", nullable = false, columnDefinition = "text")
    private String solveUrl;

    @Column(name = "solve_body", columnDefinition = "varchar(255)")
    private String solveBody;

    @Column(name = "source", columnDefinition = "varchar(255)")
    private String source;

    @Column(name = "difficulty", columnDefinition = "varchar(255)")
    private String difficulty;
}
