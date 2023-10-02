package com.selfrunner.gwalit.domain.homework.repository;

import com.selfrunner.gwalit.domain.homework.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HomeworkRepository extends JpaRepository<Homework, Long>, HomeworkRepositoryCustom {

    // 특정 학생에 해당하는 모든 숙제 반환
    Optional<List<Homework>> findAllByMemberId(Long memberId);

    // 수업과 멤버 ID에 따른 숙제 반환
    Optional<List<Homework>> findAllByMemberIdAndLessonIdAndDeletedAtIsNull(Long memberId, Long lessonId);

    void deleteAllByLessonId(Long lessonId);
}
