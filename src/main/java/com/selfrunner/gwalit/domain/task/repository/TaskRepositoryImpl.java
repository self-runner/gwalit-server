package com.selfrunner.gwalit.domain.task.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.task.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.selfrunner.gwalit.domain.lecture.entity.QLecture.lecture;
import static com.selfrunner.gwalit.domain.member.entity.QMemberAndLecture.memberAndLecture;
import static com.selfrunner.gwalit.domain.task.entity.QTask.task;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Task> findAllByMemberId(Member member) {
        return queryFactory
                .selectFrom(task)
                .leftJoin(lecture).fetchJoin()
                .leftJoin(memberAndLecture).fetchJoin()
                .where(memberAndLecture.member.eq(member))
                .fetch();
    }

}
