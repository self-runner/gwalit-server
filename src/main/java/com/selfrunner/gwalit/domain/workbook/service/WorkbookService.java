package com.selfrunner.gwalit.domain.workbook.service;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.workbook.dto.request.PostWorkbookReq;
import com.selfrunner.gwalit.domain.workbook.dto.request.PutWorkbookReq;
import com.selfrunner.gwalit.domain.workbook.dto.response.*;
import com.selfrunner.gwalit.domain.workbook.entity.Subject;
import com.selfrunner.gwalit.domain.workbook.entity.SubjectDetail;
import com.selfrunner.gwalit.domain.workbook.entity.WorkbookType;
import com.selfrunner.gwalit.domain.workbook.repository.WorkbookRepository;
import com.selfrunner.gwalit.global.util.aws.S3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorkbookService {

    private final WorkbookRepository workbookRepository;
    private final S3Client s3Client;

    @Transactional
    public List<GetSubjectRes> getSubjectList() {
        // Validation

        // Business Logic: 과목별 페이지 사용 여부 Boolean으로 표현
        List<GetSubjectRes> getSubjectResList = new ArrayList<>();
        getSubjectResList.add(new GetSubjectRes(Subject.KOREAN, Boolean.FALSE));
        getSubjectResList.add(new GetSubjectRes(Subject.MATH, Boolean.TRUE));
        getSubjectResList.add(new GetSubjectRes(Subject.ENGLISH, Boolean.FALSE));
        getSubjectResList.add(new GetSubjectRes(Subject.SOCIETY, Boolean.TRUE));
        getSubjectResList.add(new GetSubjectRes(Subject.SCIENCE, Boolean.FALSE));
        getSubjectResList.add(new GetSubjectRes(Subject.ETC, Boolean.FALSE));

        // Response
        return getSubjectResList;
    }

    @Transactional
    public List<GetSubjectMaterialRes> getSubjectMaterialList(String subject) {
        // Validation

        // Business Logic: 과목별 상세 페이지에서 콘텐츠 유무 Boolean으로 표현
        List<GetSubjectMaterialRes> getSubjectMaterialResList = new ArrayList<>();

        switch (Subject.valueOf(subject.toUpperCase())) {
            case KOREAN:

            case ENGLISH:

            case MATH:
                getSubjectMaterialResList.add(new GetSubjectMaterialRes(SubjectDetail.MATH1, Boolean.TRUE, getWorkbookTypeResList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
                getSubjectMaterialResList.add(new GetSubjectMaterialRes(SubjectDetail.MATH2, Boolean.TRUE, getWorkbookTypeResList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
                getSubjectMaterialResList.add(new GetSubjectMaterialRes(SubjectDetail.CALCULUS, Boolean.TRUE, getWorkbookTypeResList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
                getSubjectMaterialResList.add(new GetSubjectMaterialRes(SubjectDetail.STATISTICS, Boolean.TRUE, getWorkbookTypeResList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
                getSubjectMaterialResList.add(new GetSubjectMaterialRes(SubjectDetail.GEOMETRY, Boolean.TRUE, getWorkbookTypeResList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));

            case SOCIETY:
                getSubjectMaterialResList.add(new GetSubjectMaterialRes(SubjectDetail.SOCIETY_CULTURE, Boolean.TRUE, getWorkbookTypeResList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
                getSubjectMaterialResList.add(new GetSubjectMaterialRes(SubjectDetail.EVERYDAY_ETHICS, Boolean.TRUE, getWorkbookTypeResList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
                getSubjectMaterialResList.add(new GetSubjectMaterialRes(SubjectDetail.ETHICS_IDEOLOGY, Boolean.TRUE, getWorkbookTypeResList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
                getSubjectMaterialResList.add(new GetSubjectMaterialRes(SubjectDetail.LAW_POLITICS, Boolean.TRUE, getWorkbookTypeResList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));

            case SCIENCE:

            default:
        }


        // Response
        return getSubjectMaterialResList;
    }

    @Transactional
    public PostWorkbookRes registerWorkbook(Member member, PostWorkbookReq postWorkbookReq, MultipartFile workbookFile, MultipartFile thumbnailImage) {
        // Validation
        /*
        TODO: 관리자 권한 확인 코드 반영 필요
         */

        // Business Logic


        // Response
        return null;
    }

    @Transactional
    public PutWorkbookRes updateWorkbook(Member member, Long workbookId, PutWorkbookReq putWorkbookReq, MultipartFile workbookFile, MultipartFile thumbnailImage) {
        // Validation
        /*
        TODO: 관리자 권한 확인 코드 반영 필요
         */

        // Business Logic


        // Response
        return null;
    }

    @Transactional
    public Void deleteWorkbook(Member member, Long workbookId) {
        // Validation
        /*
        TODO: 관리자 권한 확인 코드 반영 필요
         */

        // Business Logic


        // Response
        return null;
    }

    public Void getOneWorkbook(Long workbookId) {
        // Validation
        /*
        TODO: 관리자 권한 확인 코드 반영 필요
         */

        // Business Logic


        // Response
        return null;
    }

    public Void getWorkbookList(Long cursorId, Long limit) {
        // Validation
        /*
        TODO: 관리자 권한 확인 코드 반영 필요
         */

        // Business Logic


        // Response
        return null;
    }

    // 과목별 탐구 내에서 사용하게 될 문제집 정보들 리스트 생성용 메소드
    public List<WorkbookTypeRes> getWorkbookTypeResList(Boolean mockExam, Boolean chapterExam, Boolean shortExam) {
        List<WorkbookTypeRes> workbookTypeResList = new ArrayList<>();
        if(mockExam.equals(Boolean.TRUE)) {
            if(chapterExam.equals(Boolean.TRUE)) {
                if(shortExam.equals(Boolean.TRUE)) {
                    // 모의고사 O, 단원평가 O, 1분 퀴즈 O
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.MOCKEXAM, Boolean.TRUE));
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.CHAPTEREXAM, Boolean.TRUE));
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.SHORTEXAM, Boolean.TRUE));
                }
                if(shortExam.equals(Boolean.FALSE)) {
                    // 모의고사 O, 단원평가 O, 1분 퀴즈 X
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.MOCKEXAM, Boolean.TRUE));
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.CHAPTEREXAM, Boolean.TRUE));
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.SHORTEXAM, Boolean.FALSE));
                }
            }
            if(chapterExam.equals(Boolean.FALSE)) {
                if(shortExam.equals(Boolean.TRUE)) {
                    // 모의고사 O, 단원평가 X, 1분 퀴즈 O
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.MOCKEXAM, Boolean.TRUE));
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.CHAPTEREXAM, Boolean.FALSE));
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.SHORTEXAM, Boolean.TRUE));
                }
                if(shortExam.equals(Boolean.FALSE)) {
                    // 모의고사 O, 단원평가 X, 1분 퀴즈 X
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.MOCKEXAM, Boolean.TRUE));
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.CHAPTEREXAM, Boolean.FALSE));
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.SHORTEXAM, Boolean.FALSE));
                }
            }
        }
        if(mockExam.equals(Boolean.FALSE)){
            if(chapterExam.equals(Boolean.TRUE)) {
                if(shortExam.equals(Boolean.TRUE)) {
                    // 모의고사 X, 단원평가 O, 1분 퀴즈 O
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.MOCKEXAM, Boolean.FALSE));
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.CHAPTEREXAM, Boolean.TRUE));
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.SHORTEXAM, Boolean.TRUE));
                }
                if(shortExam.equals(Boolean.FALSE)) {
                    // 모의고사 X, 단원평가 O, 1분 퀴즈 X
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.MOCKEXAM, Boolean.FALSE));
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.CHAPTEREXAM, Boolean.TRUE));
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.SHORTEXAM, Boolean.FALSE));
                }
            }
            if(chapterExam.equals(Boolean.FALSE)) {
                if(shortExam.equals(Boolean.TRUE)) {
                    // 모의고사 X, 단원평가 X, 1분 퀴즈 O
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.MOCKEXAM, Boolean.FALSE));
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.CHAPTEREXAM, Boolean.FALSE));
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.SHORTEXAM, Boolean.TRUE));
                }
                if(shortExam.equals(Boolean.FALSE)) {
                    // 모의고사 X, 단원평가 X, 1분 퀴즈 X
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.MOCKEXAM, Boolean.FALSE));
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.CHAPTEREXAM, Boolean.FALSE));
                    workbookTypeResList.add(new WorkbookTypeRes(WorkbookType.SHORTEXAM, Boolean.FALSE));
                }
            }
        }

        return workbookTypeResList;
    }
}
