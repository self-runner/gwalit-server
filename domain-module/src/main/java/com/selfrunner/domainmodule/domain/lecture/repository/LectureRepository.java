package com.selfrunner.domainmodule.domain.lecture.repository;

import com.selfrunner.domainmodule.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long>, LectureRepositoryCustom {
}
