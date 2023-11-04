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
import com.selfrunner.gwalit.domain.lesson.entity.Participant;
import com.selfrunner.gwalit.domain.lesson.exception.LessonException;
import com.selfrunner.gwalit.domain.lesson.repository.LessonRepository;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberAndLecture;
import com.selfrunner.gwalit.domain.member.entity.MemberMeta;
import com.selfrunner.gwalit.domain.member.exception.MemberException;
import com.selfrunner.gwalit.domain.member.repository.MemberAndLectureRepository;
import com.selfrunner.gwalit.global.common.Schedule;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.fcm.FCMClient;
import com.selfrunner.gwalit.global.util.fcm.dto.FCMMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final MemberAndLectureRepository memberAndLectureRepository;
    private final HomeworkRepository homeworkRepository;
    private final FCMClient fcmClient;

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
            // 숙제 리스트 생성
            List<Homework> tempHomeworkList = postLessonReq.getHomeworks().stream()
                    .map(homeworkReq -> HomeworkReq.staticToEntity(homeworkReq, participant.getMemberId(), lesson.getLessonId()))
                    .collect(Collectors.toList());
            homeworkList.addAll(tempHomeworkList);
        }
        homeworkRepository.saveAll(homeworkList);

        // FCM 송신 TODO: 비동기 처리를 통한 성능 향상
//        List<FCMMessageDto> fcmMessageDtoList = new ArrayList<>();
//        postLessonReq.getParticipants().stream()
//                .noneMatch(participant -> participant.getMemberId().equals(member.getMemberId())
//                .map()
//                .collect(Collectors.toList());
//        fcmClient.sendAll(fcmMessageDtoList);

        // Response
        return new LessonIdRes(lesson.getLessonId());
    }

    @Transactional
    public LessonRes update(Member member, Long lessonId, PutLessonReq putLessonReq) {
        // Validation
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new LessonException(ErrorCode.NOT_EXIST_LESSON));
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId()).orElseThrow(() -> new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic: Homework 변경 여부 확인 진행
        // lesson은 무조건 업데이트 + 변경사항이 발생하면 숙제 업데이트 진행
        lesson.update(putLessonReq);
        if((changeParticipant(lesson, putLessonReq) || changeHomework(member, lesson, putLessonReq)) && putLessonReq.getHomeworks() != null) {
            homeworkRepository.deleteHomeworkByLessonId(lessonId);
            List<Homework> homeworkInsertList = new ArrayList<>();
            for (Participant participant : putLessonReq.getParticipants()) {
                List<Homework> tempHomeworkList = putLessonReq.getHomeworks().stream()
                        .map(homeworkReq -> HomeworkReq.staticToEntity(homeworkReq, participant.getMemberId(), lesson.getLessonId()))
                        .collect(Collectors.toList());
                homeworkInsertList.addAll(tempHomeworkList);
            }
            homeworkRepository.saveAll(homeworkInsertList);
        }

        List<HomeworkRes> homeworkRes = homeworkRepository.findAllByMemberIdAndLessonId(member.getMemberId(), lessonId);
        List<MemberMeta> memberMetas = memberAndLectureRepository.findMemberMetaByLectureLectureId(lesson.getLecture().getLectureId()).orElse(null);

        // FCM 송신 TODO: 비동기 처리를 통한 성능 향상
        //fcmClient.sendAll();

        // Response
        return LessonRes.toDto(lesson, memberAndLecture.getColor(), homeworkRes, memberMetas, Boolean.TRUE);
    }

    @Transactional
    public LessonMetaRes updateMeta(Member member, Long lessonId, PatchLessonMetaRes patchLessonMetaRes) {
        // Validation
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new LessonException(ErrorCode.NOT_EXIST_LESSON));
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId()).orElseThrow(() -> new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic: 수업 정보 / 숙제 정보 업데이트
        // 수업 여부에 따라서 homework에 Participant 업데이트 진행 필요
        List<HomeworkRes> homeworkResList = homeworkRepository.findAllByMemberIdAndLessonId(member.getMemberId(), lessonId);
        if(!homeworkResList.isEmpty()) {
            List<Homework> homeworkList = new ArrayList<>();

            if(lesson.getParticipants() != null) {
                List<Long> deleteIdList = lesson.getParticipants().stream()
                        .filter(participant -> patchLessonMetaRes.getParticipants().stream().noneMatch(patchParticipant -> Objects.equals(participant.getMemberId(), patchParticipant.getMemberId())))
                        .map(Participant::getMemberId)
                        .collect(Collectors.toList());
                homeworkRepository.deleteAllByLessonIdAndMemberIdList(lessonId, deleteIdList);

                for (Participant participant : patchLessonMetaRes.getParticipants()) {
                    if(lesson.getParticipants().stream().noneMatch(lessonParticipant -> Objects.equals(lessonParticipant.getMemberId(), participant.getMemberId()))) {
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
            }

            if(lesson.getParticipants() == null) {
                for (Participant participant : patchLessonMetaRes.getParticipants()) {
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

            homeworkRepository.saveAll(homeworkList);
        }

        // 수업 정보는 무조건 업데이트 진행되므로 조건 필요 X
        lesson.updateMeta(patchLessonMetaRes);

        // FCM 송신 TODO: 비동기 처리를 통한 성능 향상
        //fcmClient.sendAll();

        // Response
        return new LessonMetaRes(lesson.getLessonId(), lesson.getLecture().getLectureId(), lesson.getType(), lesson.getDate(), new Schedule(lesson.getWeekday(), lesson.getStartTime(), lesson.getEndTime()), lesson.getParticipants());
    }

    @Transactional
    public void deleteAll(Member member, Long lectureId, List<PutLessonIdReq> putLessonIdReqList) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        List<Long> lessonIdList = putLessonIdReqList.stream()
                .map(putLessonIdReq -> putLessonIdReq.getLessonId())
                .collect(Collectors.toList());
        List<Lesson> lessonList = lessonRepository.findAllById(lessonIdList);
        lessonRepository.deleteAll(lessonList);

        // Response
    }

    @Transactional
    public void delete(Member member, Long lessonId) {
        // Validation
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new LessonException(ErrorCode.NOT_EXIST_LESSON));
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId()).orElseThrow(() -> new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        homeworkRepository.deleteAllByLessonId(lessonId);
        lessonRepository.delete(lesson);

        // Response
    }

    public LessonRes get(Member member, Long lessonId) {
        // Validation
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new LessonException((ErrorCode.NOT_EXIST_LESSON)));
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId()).orElseThrow(() -> new MemberException((ErrorCode.UNAUTHORIZED_EXCEPTION)));

        // Business Logic
        List<HomeworkRes> homeworkRes = homeworkRepository.findAllByMemberIdAndLessonId(member.getMemberId(), lessonId);
        List<MemberMeta> memberMetas = memberAndLectureRepository.findMemberMetaByLectureLectureId(lesson.getLecture().getLectureId()).orElseThrow(() -> new LessonException((ErrorCode.NOT_FOUND_EXCEPTION)));

        // Response
        return LessonRes.toDto(lesson, lesson.getLecture().getColor(), homeworkRes, memberMetas, (lesson.getCreatedAt().equals(lesson.getModifiedAt())) ? Boolean.TRUE : Boolean.FALSE);
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

    /**
     * 참여자 변동사항 확인
     * @param lesson
     * @param putLessonReq
     * @return
     */
    private boolean changeParticipant(Lesson lesson, PutLessonReq putLessonReq) {
        boolean needUpdate = Boolean.FALSE;
        if(lesson.getParticipants() == null) {
            needUpdate = Boolean.TRUE;
        }
        if(lesson.getParticipants() != null) {
            if(lesson.getParticipants().size() != putLessonReq.getParticipants().size()) {
                needUpdate = Boolean.TRUE;
            }
            if(lesson.getParticipants().size() == putLessonReq.getParticipants().size()) {
                for(Participant lessonParticipant : lesson.getParticipants()) {
                    boolean temp = Boolean.FALSE;
                    for(Participant dtoParticipant : putLessonReq.getParticipants()) {
                        if(lessonParticipant.getMemberId().equals(dtoParticipant.getMemberId())) {
                            temp = Boolean.TRUE;
                        }
                    }
                    if(!temp) {
                        needUpdate = Boolean.TRUE;
                        break;
                    }
                }
            }
        }

        return needUpdate;
    }

    /**
     * 숙제 변동사항 확인
     * @param member
     * @param lesson
     * @param putLessonReq
     * @return
     */
    private boolean changeHomework(Member member, Lesson lesson, PutLessonReq putLessonReq) {
        boolean needUpdate = Boolean.FALSE;
        List<Homework> homeworkRowList = homeworkRepository.findAllByMemberIdAndLessonIdAndDeletedAtIsNull(member.getMemberId(), lesson.getLessonId()).orElse(null);
        if(homeworkRowList != null && putLessonReq.getHomeworks() == null ) {
            needUpdate = Boolean.TRUE;
        }
        if(homeworkRowList == null && putLessonReq.getHomeworks() != null ) {
            needUpdate = Boolean.TRUE;
        }
        if(homeworkRowList != null && putLessonReq.getHomeworks() != null) {
            if(homeworkRowList.size() == putLessonReq.getHomeworks().size()) {
                for(Homework homework : homeworkRowList) {
                    for(HomeworkReq homeworkReq : putLessonReq.getHomeworks()) {
                        if(!homework.isSameHomework(homeworkReq)) {
                            needUpdate = Boolean.TRUE;
                            break;
                        }
                    }
                    if(needUpdate) {
                        break;
                    }
                }
            }
            if(homeworkRowList.size() != putLessonReq.getHomeworks().size()) {
                needUpdate = Boolean.TRUE;
            }
        }

        return needUpdate;
    }
}
