package com.selfrunner.gwalit.global.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate
    @Column(name = "createdAt", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modifiedAt", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;

//    @CreatedBy
//    @Column(name = "createdBy", updatable = false, nullable = false)
//    private Long createdId;
//
//    @LastModifiedBy
//    @Column(name = "modifiedBy", nullable = false)
//    private Long modifiedId;
}
