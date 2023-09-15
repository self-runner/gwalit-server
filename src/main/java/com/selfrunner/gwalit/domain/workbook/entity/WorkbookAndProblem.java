package com.selfrunner.gwalit.domain.workbook.entity;

import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "WorkbookAndProblem")
@SQLDelete(sql = "UPDATE workbook_and_problem set deleted_at = NOW() where workbook_and_problem_id = ?")
@Where(clause = "deleted_at IS NULL")
public class WorkbookAndProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workbook_and_problem_id", columnDefinition = "bigint")
    private Long workbookAndProblemId;
}
