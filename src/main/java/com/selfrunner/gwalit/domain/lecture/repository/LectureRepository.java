package com.selfrunner.gwalit.domain.lecture.repository;

import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long>, LectureRepositoryCustom {
}
