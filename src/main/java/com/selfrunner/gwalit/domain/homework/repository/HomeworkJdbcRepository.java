package com.selfrunner.gwalit.domain.homework.repository;

import com.selfrunner.gwalit.domain.homework.entity.Homework;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HomeworkJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<Homework> homeworkList) {
        String sql = "INSERT INTO homework (lesson_id, member_id, body, deadline, is_finish) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Homework homework = homeworkList.get(i);
                        ps.setLong(1, homework.getLessonId());
                        ps.setLong(2, homework.getMemberId());
                        ps.setString(3, homework.getBody());
                        ps.setDate(4, Date.valueOf(homework.getDeadline()));
                        ps.setBoolean(5, homework.getIsFinish());
                    }

                    @Override
                    public int getBatchSize() {
                        return homeworkList.size();
                    }
                }
        );
    }
}
