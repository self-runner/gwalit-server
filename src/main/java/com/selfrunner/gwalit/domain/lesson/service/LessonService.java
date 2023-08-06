package com.selfrunner.gwalit.domain.lesson.service;

import com.selfrunner.gwalit.domain.homework.dto.request.HomeworkReq;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkRes;
import com.selfrunner.gwalit.domain.homework.entity.Homework;
import com.selfrunner.gwalit.domain.homework.repository.HomeworkRepository;
import com.selfrunner.gwalit.domain.lesson.dto.request.PostLessonReq;
import com.selfrunner.gwalit.domain.lesson.dto.request.PutLessonIdReq;
import com.selfrunner.gwalit.domain.lesson.dto.request.PutLessonReq;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonMetaRes;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonProgressRes;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonRes;
import com.selfrunner.gwalit.domain.lesson.entity.Lesson;
import com.selfrunner.gwalit.domain.lesson.entity.Participant;
import com.selfrunner.gwalit.domain.lesson.repository.LessonRepository;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberAndLecture;
import com.selfrunner.gwalit.domain.member.repository.MemberAndLectureRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public Void register(Member member, PostLessonReq postLessonReq) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, postLessonReq.getLectureId()).orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
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
        return null;
    }

    @Transactional
    public Void registerAllDeletedLesson(Member member, Long lectureId, List<PostLessonReq> postLessonReqList) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic: 삭제를 위한 수업들은 숙제 칸이 비어 있으므로 단 건 생성 API와 분리
        List<Lesson> lessonList = postLessonReqList.stream()
                .map(postLessonReq -> PostLessonReq.staticToEntity(postLessonReq, memberAndLecture.getLecture()))
                .collect(Collectors.toList());
        lessonRepository.saveAll(lessonList);

        // Response
        return null;
    }

    @Transactional
    public Void update(Member member, Long lessonId, PutLessonReq putLessonReq) {
        // Validation
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_LESSON));
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId()).orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

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
    public Void deleteAll(Member member, Long lectureId, List<PutLessonIdReq> putLessonIdReqList) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

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
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_LESSON));
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId()).orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        lessonRepository.delete(lesson);

        // Response
        return null;
    }

    public LessonRes get(Member member, Long lessonId) {
        // Validation
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_LESSON));
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId()).orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        List<HomeworkRes> homeworkRes = homeworkRepository.findAllByMemberIdAndLessonId(member.getMemberId(), lessonId);

        LessonRes lessonRes = new LessonRes().toDto(lesson, homeworkRes);

        // Response
        return lessonRes;
    }

    public List<LessonMetaRes> getAllLessonMeta(Member member, Long lectureId) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        List<LessonMetaRes> lessonMetaRes = lessonRepository.findAllLessonMetaByLectureId(lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_LESSON));

        // Response
        return lessonMetaRes;
    }

    public List<LessonMetaRes> getAllLessonMetaByYearMonth(Member member, String year, String month) {
        // Validation

        // Business Logic
        List<Long> lectureIdList = memberAndLectureRepository.findLectureIdByMember(member).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        List<LessonMetaRes> lessonMetaRes = lessonRepository.findAllLessonMetaByYearMonth(lectureIdList, year, month).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Response
        return lessonMetaRes;
    }

    public List<LessonProgressRes> getAllProgress(Member member, Long lectureId) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        List<LessonProgressRes> lessonProgressRes = lessonRepository.findAllProgressByLectureId(lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Response
        return lessonProgressRes;
    }
}
