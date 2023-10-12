package com.selfrunner.gwalit.domain.task.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.task.dto.response.TaskRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.selfrunner.gwalit.domain.lecture.entity.QLecture.lecture;
import static com.selfrunner.gwalit.domain.member.entity.QMemberAndLecture.memberAndLecture;
import static com.selfrunner.gwalit.domain.task.entity.QTask.task;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<TaskRes>> findAllByMemberId(Member member) {
        return Optional.ofNullable(queryFactory
                .selectFrom(task)
                .innerJoin(lecture).on(task.lecture.eq(lecture)).fetchJoin()
                .innerJoin(memberAndLecture).on(lecture.eq(memberAndLecture.lecture)).fetchJoin()
                .where(memberAndLecture.member.eq(member)
                        .and(task.isPinned.isTrue()))
                .transform(groupBy(task.taskId)
                        .list(Projections.constructor(TaskRes.class, task.taskId, task.lecture.lectureId, lecture.color, task.title, task.deadline, task.isPinned, task.subtasks))));
    }

    @Override
    public Optional<List<TaskRes>> findTasksByLectureLectureIdOrderByDeadlineDesc(Long lectureId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(task)
                .innerJoin(lecture).on(task.lecture.eq(lecture)).fetchJoin()
                .where(lecture.lectureId.eq(lectureId))
                .transform(groupBy(task.taskId)
                        .list(Projections.constructor(TaskRes.class, task.taskId, task.lecture.lectureId, lecture.color, task.title, task.deadline, task.isPinned, task.subtasks))));
    }

    @Override
    public void deleteAllByLectureIdList(List<Long> lectureIdList) {
        queryFactory.update(task)
                .set(task.deletedAt, LocalDateTime.now())
                .where(task.lecture.lectureId.in(lectureIdList))
                .execute();
    }

}
