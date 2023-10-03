package com.selfrunner.gwalit.domain.task.service;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberAndLecture;
import com.selfrunner.gwalit.domain.member.exception.MemberException;
import com.selfrunner.gwalit.domain.member.repository.MemberAndLectureRepository;
import com.selfrunner.gwalit.domain.task.dto.request.PostTaskReq;
import com.selfrunner.gwalit.domain.task.dto.request.PutTaskReq;
import com.selfrunner.gwalit.domain.task.dto.response.TaskRes;
import com.selfrunner.gwalit.domain.task.entity.Task;
import com.selfrunner.gwalit.domain.task.exception.TaskException;
import com.selfrunner.gwalit.domain.task.repository.TaskRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
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
    public Void register(Member member, PostTaskReq postTaskReq) {
        // Validation: 사용자 접근 권한 확인
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, postTaskReq.getLectureId()).orElseThrow(() -> new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION));
        if(postTaskReq.getSubtasks().size() > 20) {
            throw new TaskException(ErrorCode.TOO_MANY_TASK);
        }

        // Business Logic
        Task task = postTaskReq.toEntity(memberAndLecture.getLecture());
        taskRepository.save(task);

        // Response
        return null;
    }

    @Transactional
    public Void update(Member member, Long taskId, PutTaskReq putTaskReq) {
        // Validation
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskException(ErrorCode.NOT_FOUND_EXCEPTION));
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, task.getLecture().getLectureId()).orElseThrow(() -> new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION));
        if(putTaskReq.getSubtasks().size() > 20) {
            throw new TaskException(ErrorCode.TOO_MANY_TASK);
        }

        // Business Logic
        task.update(putTaskReq);

        // Response
        return null;
    }

    @Transactional
    public Void delete(Member member, Long taskId) {
        // Validation
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskException(ErrorCode.NOT_FOUND_EXCEPTION));
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, task.getLecture().getLectureId()).orElseThrow(() -> new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        taskRepository.delete(task);

        // Response
        return null;
    }

    public List<TaskRes> getTasksByUser(Member member) {
        // Validation
//        if(!member.getType().equals(MemberType.TEACHER)) {
//            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
//        }

        // Business Login: 유저가 속한 Class 조회 및 관련 할 일들을 찾아서 반환
        List<TaskRes> taskRes = taskRepository.findAllByMemberId(member).orElseThrow(() -> new TaskException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Response
        return taskRes;
    }

    public List<TaskRes> getTasksByLecture(Member member, Long lectureId) {
        /*
        TODO: 정렬 순서는 현재 날짜 기준: 가장 가까운 날짜 / NULL / 지난 날짜 순
         */
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        List<TaskRes> taskRes = taskRepository.findTasksByLectureLectureIdOrderByDeadlineDesc(lectureId).orElseThrow(() -> new TaskException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Response
        return taskRes;
    }
}
