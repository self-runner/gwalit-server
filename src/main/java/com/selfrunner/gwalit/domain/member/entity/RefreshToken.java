package com.selfrunner.gwalit.domain.member.entity;

import com.selfrunner.gwalit.domain.member.enumerate.MemberType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "refresh_token")
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id", nullable = false)
    private Long refreshTokenId;

    @Column(name = "phone", columnDefinition = "varchar(20)", nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type", columnDefinition = "varchar(20)", nullable = false)
    private MemberType memberType;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expired_at", nullable = false)
    private Long expiredAt;

    @Builder
    public RefreshToken(String phone, MemberType memberType, String token, Long expiredAt) {
        this.phone = phone;
        this.memberType = memberType;
        this.token = token;
        this.expiredAt = expiredAt;
    }
}
