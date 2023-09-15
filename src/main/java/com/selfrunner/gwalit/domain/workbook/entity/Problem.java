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
}
