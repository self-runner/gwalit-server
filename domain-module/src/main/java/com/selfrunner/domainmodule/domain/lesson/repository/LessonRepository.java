package com.selfrunner.domainmodule.domain.lesson.repository;

import com.selfrunner.domainmodule.domain.lesson.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long>, LessonRepositoryCustom{

    void deleteAllByLectureLectureId(Long lectureId);
}
