package com.selfrunner.gwalit.domain.member.repository;

import com.selfrunner.gwalit.domain.member.entity.MemberAndNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberAndNotificationJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<MemberAndNotification> memberAndNotificationList) {
        String sql = "INSERT INTO member_and_notification (member_id, notification_id) " +
                "VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sql,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    MemberAndNotification memberAndNotification = memberAndNotificationList.get(i);
                    ps.setLong(1, memberAndNotification.getMemberId());
                    ps.setLong(2, memberAndNotification.getNotificationId());
                }

                @Override
                public int getBatchSize() {
                    return memberAndNotificationList.size();
                }
            }
        );
    }
}
