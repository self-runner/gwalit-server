package com.selfrunner.domainmodule.domain.board.repository;

import com.selfrunner.domainmodule.domain.board.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long>, FileRepositoryCustom {

    void deleteAllByUrlIn(List<String> url);
}
