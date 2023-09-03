package com.selfrunner.gwalit.domain.lesson.repository;

import com.selfrunner.gwalit.domain.lesson.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long>, LessonRepositoryCustom{

    void deleteAllByLectureLectureId(Long lectureId);
}
