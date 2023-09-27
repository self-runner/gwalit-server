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
@Table(name = "Problem")
@SQLDelete(sql = "UPDATE problem set deleted_at = NOW() where problem_id = ?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(name = "problem_url",  columnDefinition = "text")
    private String problemUrl;

    @Column(name = "problem_body", columnDefinition = "varchar(255)")
    private String problemBody;

    @Column(name = "answer", nullable = false, columnDefinition = "int")
    private Integer answer;

    @Column(name = "solve_url", nullable = false, columnDefinition = "text")
    private String solveUrl;

    @Column(name = "solve_body", columnDefinition = "varchar(255)")
    private String solveBody;

    @Column(name = "source", columnDefinition = "varchar(255)")
    private String source;

    @Column(name = "difficulty", columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Builder
    public Problem(String subject, String subjectDetail, String type, String problemUrl, String problemBody, Integer answer, String solveUrl, String solveBody, String source, String difficulty) {
        this.subject = Subject.valueOf(subject);
        this.subjectDetail = SubjectDetail.valueOf(subjectDetail);
        this.type = ProblemType.valueOf(type);
        this.problemUrl = problemUrl;
        this.problemBody = problemBody;
        this.answer = answer;
        this.solveUrl = solveUrl;
        this.solveBody = solveBody;
        this.source = source;
        this.difficulty = Difficulty.valueOf(difficulty);
    }
}
