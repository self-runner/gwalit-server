package com.selfrunner.gwalit.domain.log.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logId")
    private Long logId;

    @Column(name = "memberId")
    private Long memberId;

    @Column(name = "endPoint", columnDefinition = "TEXT")
    private String endPoint;

    @Column(name = "isSuccess")
    private Boolean isSuccess;

    @Builder
    public Log(Long memberId, String endPoint, Boolean isSuccess) {
        this.memberId = memberId;
        this.endPoint = endPoint;
        this.isSuccess = isSuccess;
    }
}
