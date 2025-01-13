package com.selfrunner.apimodule.application.workbook;

import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;
import com.selfrunner.domainmodule.domain.member.Member;
import com.selfrunner.commonmodule.dto.workbook.request.WorkbookReq;
import com.selfrunner.commonmodule.dto.workbook.response.WorkbookCardRes;
import com.selfrunner.commonmodule.dto.workbook.response.WorkbookRes;
import com.selfrunner.commonmodule.enumerate.workbook.SubjectDetail;
import com.selfrunner.domainmodule.domain.workbook.Views;
import com.selfrunner.domainmodule.domain.workbook.Workbook;
import com.selfrunner.domainmodule.domain.workbook.repository.ViewsRepository;
import com.selfrunner.domainmodule.domain.workbook.repository.WorkbookRepository;
import com.selfrunner.inframodule.s3.S3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorkbookService {

    private final WorkbookRepository workbookRepository;
    private final ViewsRepository viewsRepository;
    private final S3Client s3Client;


    @Transactional
    public WorkbookRes registerWorkbook(Member member, WorkbookReq workbookReq, MultipartFile thumbnailImage, MultipartFile thumbnailCardImage, MultipartFile workbookFile, MultipartFile answerFile) {
        // Validation
        /*
        TODO: 관리자 권한 확인 코드 반영 필요
         */

        // Business Logic: 이미지, 파일 등 등록 이후, Entity로 저장
        String thumbnailUrl, thumbnailCardUrl, workbookFileUrl, answerFileUrl;
        // 문제집 파일을 직접 올릴 경우
        if(workbookReq.getIsFile().equals(Boolean.TRUE)) {
            try {
                thumbnailUrl = s3Client.upload(thumbnailImage, "workbook/" + workbookReq.getType());
                thumbnailCardUrl = s3Client.upload(thumbnailCardImage, "workbook/" + workbookReq.getType());
                workbookFileUrl = s3Client.upload(workbookFile, "workbook/" + workbookReq.getType());
                answerFileUrl = s3Client.upload(answerFile, "workbook/" + workbookReq.getType());
                Views views = Views.builder().count(0).build();
                Views savedViews = viewsRepository.save(views);
                Workbook workbook = Workbook.builder()
                        .views(savedViews)
                        .title(workbookReq.getTitle())
                        .type(workbookReq.getType())
                        .subject(workbookReq.getSubject())
                        .subjectDetail(workbookReq.getSubjectDetail())
                        .chapter(workbookReq.getChapter())
                        .thumbnailUrl(thumbnailUrl)
                        .thumbnailCardUrl(thumbnailCardUrl)
                        .workbookFileUrl(workbookFileUrl)
                        .answerFileUrl(answerFileUrl)
                        .problemCount(workbookReq.getProblemCount())
                        .time(workbookReq.getTime())
                        .explanation(workbookReq.getExplanation())
                        .provider(workbookReq.getProvider())
                        .copyright(workbookReq.getCopyright())
                        .isFile(workbookReq.getIsFile())
                        .build();
                Workbook saveWorkbook = workbookRepository.save(workbook);

                return new WorkbookRes(saveWorkbook.getWorkbookId(),
                        saveWorkbook.getTitle(),
                        saveWorkbook.getType().getKoreanName(),
                        saveWorkbook.getSubject().getKoreanName(),
                        saveWorkbook.getSubjectDetail().getKoreanName(),
                        saveWorkbook.getChapter(),
                        saveWorkbook.getThumbnailUrl(),
                        saveWorkbook.getWorkbookFileUrl(),
                        saveWorkbook.getAnswerFileUrl(),
                        saveWorkbook.getProblemCount(),
                        saveWorkbook.getTime(),
                        saveWorkbook.getExplanation(),
                        saveWorkbook.getProvider(),
                        saveWorkbook.getCopyright(),
                        saveWorkbook.getIsFile(),
                        saveWorkbook.getViews().getCount(),
                        saveWorkbook.getModifiedAt(),
                        null);

            } catch (Exception e) {
                throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
            }

        }
        // 문제를 별도로 등록하여, 참조 테이블로 엮어서 만드는 경우
        if(workbookReq.getIsFile().equals(Boolean.FALSE)) {
            try {
                thumbnailUrl = s3Client.upload(thumbnailImage, "workbook/" + workbookReq.getType());
                thumbnailCardUrl = s3Client.upload(thumbnailCardImage, "workbook/" + workbookReq.getType());
                Views views = Views.builder().count(0).build();
                Views savedViews = viewsRepository.save(views);
                Workbook workbook = Workbook.builder()
                        .views(savedViews)
                        .title(workbookReq.getTitle())
                        .type(workbookReq.getType())
                        .subject(workbookReq.getSubject())
                        .subjectDetail(workbookReq.getSubjectDetail())
                        .chapter(workbookReq.getChapter())
                        .thumbnailUrl(thumbnailUrl)
                        .thumbnailCardUrl(thumbnailCardUrl)
                        .problemCount(workbookReq.getProblemCount())
                        .time(workbookReq.getTime())
                        .explanation(workbookReq.getExplanation())
                        .provider(workbookReq.getProvider())
                        .copyright(workbookReq.getCopyright())
                        .isFile(workbookReq.getIsFile())
                        .build();
                Workbook saveWorkbook = workbookRepository.save(workbook);

                return new WorkbookRes(saveWorkbook.getWorkbookId(),
                        saveWorkbook.getTitle(),
                        saveWorkbook.getType().getKoreanName(),
                        saveWorkbook.getSubject().getKoreanName(),
                        saveWorkbook.getSubjectDetail().getKoreanName(),
                        saveWorkbook.getChapter(),
                        saveWorkbook.getThumbnailUrl(),
                        saveWorkbook.getWorkbookFileUrl(),
                        saveWorkbook.getAnswerFileUrl(),
                        saveWorkbook.getProblemCount(),
                        saveWorkbook.getTime(),
                        saveWorkbook.getExplanation(),
                        saveWorkbook.getProvider(),
                        saveWorkbook.getCopyright(),
                        saveWorkbook.getIsFile(),
                        saveWorkbook.getViews().getCount(),
                        saveWorkbook.getModifiedAt(),
                        null);
            } catch (Exception e) {
                throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
            }
        }

        // Response
        return null;
    }

    @Transactional
    public WorkbookRes updateWorkbook(Member member, Long workbookId, WorkbookReq workbookReq, MultipartFile thumbnailImage, MultipartFile thumbnailCardImage, MultipartFile workbookFile, MultipartFile answerFile) {
        // Validation
        /*
        TODO: 관리자 권한 확인 코드 반영 필요
         */
        Workbook workbook = workbookRepository.findById(workbookId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Business Logic: 이미지, 파일 등 등록 이후, Entity로 업데이트
        // 문제집 파일을 직접 올릴 경우
        if(workbookReq.getIsFile().equals(Boolean.TRUE)) {
            try {
                if(thumbnailImage != null && !thumbnailImage.isEmpty()) {
                    s3Client.delete(workbook.getThumbnailUrl());
                    String updateThumbnailImageUrl = s3Client.upload(thumbnailImage, "workbook/" + workbookReq.getType());
                    workbook.updateThumbnailUrl(updateThumbnailImageUrl);
                }
                if(thumbnailCardImage != null && !thumbnailCardImage.isEmpty()) {
                    if(workbook.getThumbnailCardUrl() != null) {
                        s3Client.delete(workbook.getThumbnailCardUrl());
                    }
                    String updateThumbnailCardImageUrl = s3Client.upload(thumbnailCardImage, "workbook/" + workbookReq.getType());
                    workbook.updateThumbnailCardUrl(updateThumbnailCardImageUrl);
                }
                if(workbookFile != null && !workbookFile.isEmpty()) {
                    s3Client.delete(workbook.getWorkbookFileUrl());
                    String updateWorkbookFileUrl = s3Client.upload(workbookFile, "workbook/" + workbookReq.getType());
                    workbook.updateWorkbookFileUrl(updateWorkbookFileUrl);
                }
                if(answerFile != null && !answerFile.isEmpty()) {
                    s3Client.delete(workbook.getAnswerFileUrl());
                    String updateAnswerFileUrl = s3Client.upload(answerFile, "workbook/" + workbookReq.getType());
                    workbook.updateAnswerFileUrl(updateAnswerFileUrl);
                }
                workbook.update(workbookReq);
                Workbook updateWorkbook = workbookRepository.save(workbook);

                return new WorkbookRes(updateWorkbook.getWorkbookId(),
                        updateWorkbook.getTitle(),
                        updateWorkbook.getType().getKoreanName(),
                        updateWorkbook.getSubject().getKoreanName(),
                        updateWorkbook.getSubjectDetail().getKoreanName(),
                        updateWorkbook.getChapter(),
                        updateWorkbook.getThumbnailUrl(),
                        updateWorkbook.getWorkbookFileUrl(),
                        updateWorkbook.getAnswerFileUrl(),
                        updateWorkbook.getProblemCount(),
                        updateWorkbook.getTime(),
                        updateWorkbook.getExplanation(),
                        updateWorkbook.getProvider(),
                        updateWorkbook.getCopyright(),
                        updateWorkbook.getIsFile(),
                        updateWorkbook.getViews().getCount(),
                        updateWorkbook.getModifiedAt(),
                        null);

            } catch (Exception e) {
                throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
            }

        }
        // 문제를 별도로 등록하여, 참조 테이블로 엮어서 만드는 경우
        if(workbookReq.getIsFile().equals(Boolean.FALSE)) {
            try {
                if(thumbnailImage != null && !thumbnailImage.isEmpty()) {
                    s3Client.delete(workbook.getThumbnailUrl());
                    String updateThumbnailImageUrl = s3Client.upload(thumbnailImage, "workbook/" + workbookReq.getType());
                    workbook.updateThumbnailUrl(updateThumbnailImageUrl);
                }
                if(thumbnailCardImage != null && !thumbnailCardImage.isEmpty()) {
                    if(workbook.getThumbnailCardUrl() != null) {
                        s3Client.delete(workbook.getThumbnailCardUrl());
                    }
                    String updateThumbnailImageUrl = s3Client.upload(thumbnailCardImage, "workbook/" + workbookReq.getType());
                    workbook.updateThumbnailUrl(updateThumbnailImageUrl);
                }
                workbook.update(workbookReq);
                Workbook updateWorkbook = workbookRepository.save(workbook);

                return new WorkbookRes(updateWorkbook.getWorkbookId(),
                        updateWorkbook.getTitle(),
                        updateWorkbook.getType().getKoreanName(),
                        updateWorkbook.getSubject().getKoreanName(),
                        updateWorkbook.getSubjectDetail().getKoreanName(),
                        updateWorkbook.getChapter(),
                        updateWorkbook.getThumbnailUrl(),
                        updateWorkbook.getWorkbookFileUrl(),
                        updateWorkbook.getAnswerFileUrl(),
                        updateWorkbook.getProblemCount(),
                        updateWorkbook.getTime(),
                        updateWorkbook.getExplanation(),
                        updateWorkbook.getProvider(),
                        updateWorkbook.getCopyright(),
                        updateWorkbook.getIsFile(),
                        updateWorkbook.getViews().getCount(),
                        updateWorkbook.getModifiedAt(),
                        null);
            } catch (Exception e) {
                throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
            }
        }

        // Response
        return null;
    }

    @Transactional
    public void deleteWorkbook(Member member, Long workbookId) {
        // Validation
        /*
        TODO: 관리자 권한 확인 코드 반영 필요
         */
        Workbook workbook = workbookRepository.findById(workbookId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Business Logic
        // 문제집 파일을 직접 올릴 경우
        workbookRepository.delete(workbook);
        if(workbook.getIsFile().equals(Boolean.TRUE)) {
            try {
                s3Client.delete(workbook.getThumbnailUrl());
                s3Client.delete(workbook.getWorkbookFileUrl());
                s3Client.delete(workbook.getAnswerFileUrl());
            } catch (Exception e) {
                throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
            }

        }
        // 문제를 별도로 등록하여, 참조 테이블로 엮어서 만드는 경우
        if(workbook.getIsFile().equals(Boolean.FALSE)) {
            try {
                s3Client.delete(workbook.getThumbnailUrl());
            } catch (Exception e) {
                throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
            }
        }

        // Response
    }

    @Transactional
    public WorkbookRes getOneWorkbook(Member member, Long workbookId) {
        // Validation: Controller에서 @Auth를 통해 인증된 사용자만 접근할 수 있도록 함.

        // Business Logic
        Workbook workbook = workbookRepository.findById(workbookId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        workbook.getViews().update();
        Workbook updateWorkbook = workbookRepository.save(workbook);

        // Response
        // 추후, 문제 테이블을 만들어 문제별로 관리하게 된다면, 난이도 부분을 별도로 적용해야 함을 감안해 미리 생성 후 null로 바인딩.
        return new WorkbookRes(updateWorkbook.getWorkbookId(),
                updateWorkbook.getTitle(),
                updateWorkbook.getType().getKoreanName(),
                updateWorkbook.getSubject().getKoreanName(),
                updateWorkbook.getSubjectDetail().getKoreanName(),
                updateWorkbook.getChapter(),
                updateWorkbook.getThumbnailUrl(),
                updateWorkbook.getWorkbookFileUrl(),
                updateWorkbook.getAnswerFileUrl(),
                updateWorkbook.getProblemCount(),
                updateWorkbook.getTime(),
                updateWorkbook.getExplanation(),
                updateWorkbook.getProvider(),
                updateWorkbook.getCopyright(),
                updateWorkbook.getIsFile(),
                updateWorkbook.getViews().getCount(),
                updateWorkbook.getModifiedAt(),
                null);
    }

    public List<WorkbookCardRes> getMainWorkbookList(Member member) {
        // Validation: Controller에서 @Auth를 통해 인증된 사용자만 접근할 수 있도록 함.

        // Business Logic && Response
        return workbookRepository.findAllByCreatedAtDescAndLimit(4L).orElse(null);
    }

    public Slice<WorkbookCardRes> getWorkbookList(Member member, String detail, String type, Long cursor, Pageable pageable) {
        // Validation: Controller에서 @Auth를 통해 인증된 사용자만 접근할 수 있도록 함.

        // Business Logic
        SubjectDetail subjectDetail = getSubjectDetail(detail);

        LocalDateTime cursorCreatedAt = (cursor != null)
                ? workbookRepository.findById(cursor).get().getCreatedAt()
                : null;

        // Response
        return workbookRepository.findWorkbookCardPageableBy(subjectDetail, type, cursor, cursorCreatedAt, pageable);
    }

    // detail 과목 정보 조회 메소드
    private static SubjectDetail getSubjectDetail(String detail) {
        SubjectDetail subjectDetail = null;
        SubjectDetail[] subjectDetailList = SubjectDetail.values();
        for(SubjectDetail temp : subjectDetailList) {
            if(temp.getEnglishName().equals(detail)) {
                subjectDetail = temp;
                break;
            }
        }
        if(subjectDetail == null) {
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }
        return subjectDetail;
    }
}
