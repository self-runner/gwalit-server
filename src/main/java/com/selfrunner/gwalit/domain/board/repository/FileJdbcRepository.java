package com.selfrunner.gwalit.domain.board.repository;

import com.selfrunner.gwalit.domain.board.entity.File;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FileJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<File> fileList) {
        String sql = "INSERT INTO file (name, url, size, member_id, lecture_id, board_id, reply_id)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                        File file = fileList.get(i);
                        ps.setString(1, file.getName());
                        ps.setString(2, file.getUrl());
                        ps.setLong(3, file.getSize());
                        ps.setLong(4, file.getMemberId());
                        ps.setLong(5, file.getLectureId());
                        ps.setLong(6, file.getBoardId());

                        Long replyId = file.getReplyId();
                        if(replyId != null) {
                            ps.setLong(7, file.getReplyId());
                        }
                        else {
                            ps.setObject(7, null);
                        }
                    }

                    @Override
                    public int getBatchSize() {
                        return fileList.size();
                    }
                }
        );
    }
}
