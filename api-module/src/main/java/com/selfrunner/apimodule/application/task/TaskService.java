package com.selfrunner.apimodule.application.task;

import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;
import com.selfrunner.domainmodule.domain.member.Member;
import com.selfrunner.domainmodule.domain.member.MemberAndLecture;
import com.selfrunner.domainmodule.domain.member.repository.MemberAndLectureRepository;
import com.selfrunner.commonmodule.dto.task.request.PostTaskReq;
import com.selfrunner.commonmodule.dto.task.request.PutTaskReq;
import com.selfrunner.commonmodule.dto.task.response.TaskRes;
import com.selfrunner.domainmodule.domain.task.Task;
import com.selfrunner.domainmodule.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final MemberAndLectureRepository memberAndLectureRepository;

    @Transactional
    public TaskRes register(Member member, PostTaskReq postTaskReq) {
        // Validation: 사용자 접근 권한 확인
        MemberAndLecture memberAndLecture =
                memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, postTaskReq.getLectureId())
                        .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));
        if(postTaskReq.getSubtasks().size() > 20) {
            throw new ApplicationException(ErrorCode.TOO_MANY_TASK);
        }

        // Business Logic
        Task task = Task.builder()
                .lecture(memberAndLecture.getLecture())
                .title(postTaskReq.getTitle())
                .deadline(postTaskReq.getDeadline())
                .isPinned(postTaskReq.getIsPinned())
                .subtasks(postTaskReq.getSubtasks())
                .build();
        taskRepository.save(task);

        // Response
        return new TaskRes(task.getTaskId(),
                task.getLecture().getLectureId(),
                task.getLecture().getColor(),
                task.getTitle(),
                task.getDeadline(),
                task.getIsPinned(),
                task.getSubtasks());
    }

    @Transactional
    public TaskRes update(Member member, Long taskId, PutTaskReq putTaskReq) {
        // Validation
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, task.getLecture().getLectureId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));
        if(putTaskReq.getSubtasks().size() > 20) {
            throw new ApplicationException(ErrorCode.TOO_MANY_TASK);
        }

        // Business Logic
        task.update(putTaskReq);

        // Response
        return new TaskRes(task.getTaskId(),
                task.getLecture().getLectureId(),
                task.getLecture().getColor(),
                task.getTitle(),
                task.getDeadline(),
                task.getIsPinned(),
                task.getSubtasks());
    }

    @Transactional
    public void delete(Member member, Long taskId) {
        // Validation
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, task.getLecture().getLectureId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        taskRepository.delete(task);

        // Response
    }

    public List<TaskRes> getTasksByUser(Member member) {
        // Validation
//        if(!member.getType().equals(MemberType.TEACHER)) {
//            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
//        }

        // Business Login: 유저가 속한 Class 조회 및 관련 할 일들을 찾아서 반환 && Response
        return taskRepository.findAllByMemberId(member)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
    }

    public List<TaskRes> getTasksByLecture(Member member, Long lectureId) {
        /*
        TODO: 정렬 순서는 현재 날짜 기준: 가장 가까운 날짜 / NULL / 지난 날짜 순
         */
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic && Response
        return taskRepository.findTasksByLectureLectureIdOrderByDeadlineDesc(lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
    }
}
