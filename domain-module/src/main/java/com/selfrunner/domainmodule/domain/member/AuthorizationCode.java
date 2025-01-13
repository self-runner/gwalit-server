package com.selfrunner.domainmodule.domain.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "authorization_code")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorizationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authorization_code_id")
    private Long authorizationCodeId;

    @Column(name = "phone", columnDefinition = "varchar(20)", nullable = false)
    private String phone;

    @Column(name = "authorization_code", columnDefinition = "varchar(6)", nullable = false)
    private String authorizationCode;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @Builder
    public AuthorizationCode(String phone, String authorizationCode) {
        this.phone = phone;
        this.authorizationCode = authorizationCode;
        this.sentAt = LocalDateTime.now();
        this.expiredAt = LocalDateTime.now().plusMinutes(5);
    }
}
