package com.selfrunner.gwalit.domain.lesson.service;

import com.selfrunner.gwalit.domain.homework.dto.request.HomeworkReq;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkRes;
import com.selfrunner.gwalit.domain.homework.entity.Homework;
import com.selfrunner.gwalit.domain.homework.repository.HomeworkRepository;
import com.selfrunner.gwalit.domain.lecture.exception.LectureException;
import com.selfrunner.gwalit.domain.lesson.dto.request.PatchLessonMetaRes;
import com.selfrunner.gwalit.domain.lesson.dto.request.PostLessonReq;
import com.selfrunner.gwalit.domain.lesson.dto.request.PutLessonIdReq;
import com.selfrunner.gwalit.domain.lesson.dto.request.PutLessonReq;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonIdRes;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonMetaRes;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonProgressRes;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonRes;
import com.selfrunner.gwalit.domain.lesson.entity.Lesson;
import com.selfrunner.gwalit.domain.lesson.entity.LessonType;
import com.selfrunner.gwalit.domain.lesson.entity.Participant;
import com.selfrunner.gwalit.domain.lesson.exception.LessonException;
import com.selfrunner.gwalit.domain.lesson.repository.LessonRepository;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberAndLecture;
import com.selfrunner.gwalit.domain.member.entity.MemberMeta;
import com.selfrunner.gwalit.domain.member.exception.MemberException;
import com.selfrunner.gwalit.domain.member.repository.MemberAndLectureRepository;
import com.selfrunner.gwalit.global.common.Schedule;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final MemberAndLectureRepository memberAndLectureRepository;
    private final HomeworkRepository homeworkRepository;

    @Transactional
    public LessonIdRes register(Member member, PostLessonReq postLessonReq) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, postLessonReq.getLectureId()).orElseThrow(() -> new MemberException((ErrorCode.UNAUTHORIZED_EXCEPTION)));
        if(memberAndLecture.getIsTeacher().equals(Boolean.FALSE)) {
            throw new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic
        // 정규 수업 정보 등록
        Lesson lesson = postLessonReq.toEntity(memberAndLecture.getLecture());
        lessonRepository.save(lesson);
        List<Homework> homeworkList = new ArrayList<>();
        for (Participant participant : postLessonReq.getParticipants()) {
            List<Homework> tempHomeworkList = postLessonReq.getHomeworks().stream()
                    .map(homeworkReq -> HomeworkReq.staticToEntity(homeworkReq, participant.getMemberId(), lesson.getLessonId()))
                    .collect(Collectors.toList());
            homeworkList.addAll(tempHomeworkList);
        }
        homeworkRepository.saveAll(homeworkList);

        // Response
        LessonIdRes lessonIdRes = new LessonIdRes(lesson.getLessonId());
        return lessonIdRes;
    }

//    @Transactional
//    public Void registerAllDeletedLesson(Member member, Long lectureId, List<PostLessonReq> postLessonReqList) {
//        // Validation
//        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));
//
//        // Business Logic: 삭제를 위한 수업들은 숙제 칸이 비어 있으므로 단 건 생성 API와 분리
//        List<Lesson> lessonList = postLessonReqList.stream()
//                .map(postLessonReq -> PostLessonReq.staticToEntity(postLessonReq, memberAndLecture.getLecture()))
//                .collect(Collectors.toList());
//        lessonRepository.saveAll(lessonList);
//
//        // Response
//        return null;
//    }

    @Transactional
    public Void update(Member member, Long lessonId, PutLessonReq putLessonReq) {
        // Validation
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new LessonException(ErrorCode.NOT_EXIST_LESSON));
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId()).orElseThrow(() -> new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        lesson.update(putLessonReq);
        homeworkRepository.deleteHomeworkByLessonId(lessonId);
        List<Homework> homeworkList = new ArrayList<>();
        for (Participant participant : putLessonReq.getParticipants()) {
            List<Homework> tempHomeworkList = putLessonReq.getHomeworks().stream()
                    .map(homeworkReq -> HomeworkReq.staticToEntity(homeworkReq, participant.getMemberId(), lesson.getLessonId()))
                    .collect(Collectors.toList());
            homeworkList.addAll(tempHomeworkList);
        }
        homeworkRepository.saveAll(homeworkList);

        // Response
        return null;
    }

    @Transactional
    public LessonMetaRes updateMeta(Member member, Long lessonId, PatchLessonMetaRes patchLessonMetaRes) {
        // Validation
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new LessonException(ErrorCode.NOT_EXIST_LESSON));
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId()).orElseThrow(() -> new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        // 수업 여부에 따라서 homework에 Participant 업데이트 진행 필요
        List<Long> deleteIdList = lesson.getParticipants().stream()
                .filter(participant -> patchLessonMetaRes.getParticipants().stream().noneMatch(patchParticipant -> participant.getMemberId() == patchParticipant.getMemberId()))
                .map(Participant::getMemberId)
                .collect(Collectors.toList());
        homeworkRepository.deleteAllByLessonIdAndMemberIdList(lessonId, deleteIdList);
        List<HomeworkRes> homeworkResList = homeworkRepository.findAllByMemberIdAndLessonId(member.getMemberId(), lessonId);
        List<Homework> homeworkList = new ArrayList<>();
        for (Participant participant : patchLessonMetaRes.getParticipants()) {
            if(lesson.getParticipants().stream().noneMatch(lessonParticipant -> lessonParticipant.getMemberId() == participant.getMemberId())) {
                System.out.println("t" + participant.getMemberId());
                List<Homework> tempHomeworkList = homeworkResList.stream()
                        .map(homeworkRes -> {
                            // homeworkRes에서 필요한 내용을 가져와서 Homework.builder()를 사용하여 Homework 객체를 생성
                            String body = homeworkRes.getBody();
                            LocalDate deadLine = homeworkRes.getDeadline();
                            // homeworkRes 내의 값을 사용하여 Homework 객체 생성
                            Homework homework = Homework.builder()
                                    .lessonId(lessonId)
                                    .memberId(participant.getMemberId())
                                    .body(body)
                                    .deadline(deadLine)
                                    .isFinish(Boolean.FALSE)
                                    .build();

                            return homework;
                        })
                        .collect(Collectors.toList());
                homeworkList.addAll(tempHomeworkList);
            }

        }
        lesson.updateMeta(patchLessonMetaRes);
        System.out.println("size: " + homeworkList.size());
        homeworkRepository.saveAll(homeworkList);


        // Response
        return new LessonMetaRes(lesson.getLessonId(), lesson.getLecture().getLectureId(), lesson.getType(), lesson.getDate(), new Schedule(lesson.getWeekday(), lesson.getStartTime(), lesson.getEndTime()), lesson.getParticipants());
    }

    @Transactional
    public Void deleteAll(Member member, Long lectureId, List<PutLessonIdReq> putLessonIdReqList) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        List<Long> lessonIdList = putLessonIdReqList.stream()
                .map(putLessonIdReq -> putLessonIdReq.getLessonId())
                .collect(Collectors.toList());
        List<Lesson> lessonList = lessonRepository.findAllById(lessonIdList);
        lessonRepository.deleteAll(lessonList);

        // Response
        return null;
    }

    @Transactional
    public Void delete(Member member, Long lessonId) {
        // Validation
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new LessonException(ErrorCode.NOT_EXIST_LESSON));
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId()).orElseThrow(() -> new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        homeworkRepository.deleteAllByLessonId(lessonId);
        lessonRepository.delete(lesson);

        // Response
        return null;
    }

    public LessonRes get(Member member, Long lessonId) {
        // Validation
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new LessonException((ErrorCode.NOT_EXIST_LESSON)));
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId()).orElseThrow(() -> new MemberException((ErrorCode.UNAUTHORIZED_EXCEPTION)));

        // Business Logic
        List<HomeworkRes> homeworkRes = homeworkRepository.findAllByMemberIdAndLessonId(member.getMemberId(), lessonId);
        List<MemberMeta> memberMetas = memberAndLectureRepository.findMemberMetaByLectureLectureId(lesson.getLecture().getLectureId()).orElseThrow(() -> new LessonException((ErrorCode.NOT_FOUND_EXCEPTION)));

        LessonRes lessonRes = new LessonRes().toDto(lesson, lesson.getLecture().getColor(), homeworkRes, memberMetas, (lesson.getCreatedAt().equals(lesson.getModifiedAt())) ? Boolean.TRUE : Boolean.FALSE);

        // Response
        return lessonRes;
    }

    public List<LessonMetaRes> getAllLessonMeta(Member member, Long lectureId) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new MemberException((ErrorCode.UNAUTHORIZED_EXCEPTION)));

        // Business Logic
        List<LessonMetaRes> lessonMetaRes = lessonRepository.findAllLessonMetaByLectureId(lectureId).orElseThrow(() -> new LessonException((ErrorCode.NOT_EXIST_LESSON)));
        // 오름차순 정렬
        Collections.sort(lessonMetaRes);

        // Response
        return lessonMetaRes;
    }

    public List<LessonMetaRes> getAllLessonMetaByYearMonth(Member member, String year, String month) {
        // Validation

        // Business Logic
        List<Long> lectureIdList = memberAndLectureRepository.findLectureIdByMember(member).orElseThrow(() -> new LectureException((ErrorCode.NOT_FOUND_EXCEPTION)));
        List<LessonMetaRes> lessonMetaRes = lessonRepository.findAllLessonMetaByYearMonth(lectureIdList, year, month).orElse(null);
        // 오름차순 정렬
        Collections.sort(lessonMetaRes);

        // Response
        return lessonMetaRes;
    }

    public List<LessonProgressRes> getAllProgress(Member member, Long lectureId) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new MemberException((ErrorCode.UNAUTHORIZED_EXCEPTION)));

        // Business Logic
        List<LessonProgressRes> lessonProgressRes = lessonRepository.findAllProgressByLectureId(lectureId).orElseThrow(() -> new LessonException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Response
        return lessonProgressRes;
    }
}
