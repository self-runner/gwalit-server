package com.selfrunner.gwalit.domain.board.repository;

import com.selfrunner.gwalit.domain.board.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long>, FileRepositoryCustom {

    void deleteAllByUrlIn(List<String> url);
}
