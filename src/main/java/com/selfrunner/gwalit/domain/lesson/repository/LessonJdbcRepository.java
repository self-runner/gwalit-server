package com.selfrunner.gwalit.domain.lesson.repository;

import com.selfrunner.gwalit.domain.lesson.entity.Lesson;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LessonJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<Lesson> lessonList) {
        String sql = "INSERT INTO lesson (lecture_id, type, date, weekday, start_time, end_time) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Lesson lesson = lessonList.get(i);
                        ps.setLong(1, lesson.getLecture().getLectureId());
                        ps.setString(2, lesson.getType().toString());
                        ps.setDate(3, Date.valueOf(lesson.getDate()));
                        ps.setString(4, lesson.getWeekday().toString());
                        ps.setTime(5, Time.valueOf(lesson.getStartTime()));
                        ps.setTime(6, Time.valueOf(lesson.getEndTime()));
                    }

                    @Override
                    public int getBatchSize() {
                        return lessonList.size();
                    }
                }

        );
    }
}
