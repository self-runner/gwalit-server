package com.selfrunner.gwalit.domain.setting.entity;

import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Setting")
@TypeDef(name = "json", typeClass = JsonType.class)
@SQLDelete(sql = "UPDATE setting SET deleted_at = NOW() where setting_id = ?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Setting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settingId")
    private Long settingId;

    @Type(type = "json")
    @Column(name = "inform", columnDefinition = "json")
    private Inform inform;

    public void update(Inform inform) {
        this.inform = inform;
    }

    @Builder
    public Setting(Inform inform) {
        this.inform = inform;
    }
}
