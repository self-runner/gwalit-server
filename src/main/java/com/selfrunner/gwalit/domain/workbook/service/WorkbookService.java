package com.selfrunner.gwalit.domain.workbook.service;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.workbook.dto.request.WorkbookReq;
import com.selfrunner.gwalit.domain.workbook.dto.response.*;
import com.selfrunner.gwalit.domain.workbook.entity.Views;
import com.selfrunner.gwalit.domain.workbook.entity.Workbook;
import com.selfrunner.gwalit.domain.workbook.exception.WorkbookException;
import com.selfrunner.gwalit.domain.workbook.repository.ViewsRepository;
import com.selfrunner.gwalit.domain.workbook.repository.WorkbookRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.aws.S3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    private final ViewsRepository viewsRepository;
    private final S3Client s3Client;


    @Transactional
    public WorkbookRes registerWorkbook(Member member, WorkbookReq workbookReq, MultipartFile thumbnailImage, MultipartFile workbookFile, MultipartFile answerFile) {
        // Validation
        /*
        TODO: 관리자 권한 확인 코드 반영 필요
         */

        // Business Logic: 이미지, 파일 등 등록 이후, Entity로 저장
        String thumbnailUrl, workbookFileUrl, answerFileUrl;
        // 문제집 파일을 직접 올릴 경우
        if(workbookReq.getIsFile().equals(Boolean.TRUE)) {
            try {
                thumbnailUrl = s3Client.upload(thumbnailImage, "workbook/" + workbookReq.getType());
                workbookFileUrl = s3Client.upload(workbookFile, "workbook/" + workbookReq.getType());
                answerFileUrl = s3Client.upload(answerFile, "workbook/" + workbookReq.getType());
                Views views = Views.builder().count(0).build();
                Views savedViews = viewsRepository.save(views);
                Workbook workbook = workbookReq.toFileEntity(savedViews, thumbnailUrl, workbookFileUrl, answerFileUrl);
                workbookRepository.save(workbook);

                return new WorkbookRes(workbook, null);

            } catch (Exception e) {
                throw new WorkbookException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
            }

        }
        // 문제를 별도로 등록하여, 참조 테이블로 엮어서 만드는 경우
        if(workbookReq.getIsFile().equals(Boolean.FALSE)) {
            try {
                thumbnailUrl = s3Client.upload(thumbnailImage, "workbook/" + workbookReq.getType());
                Views views = Views.builder().count(0).build();
                Views savedViews = viewsRepository.save(views);
                Workbook workbook = workbookReq.toNonFileEntity(savedViews, thumbnailUrl);
                workbookRepository.save(workbook);

                return new WorkbookRes(workbook, null);
            } catch (Exception e) {
                throw new WorkbookException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
            }
        }

        // Response
        return null;
    }

    @Transactional
    public WorkbookRes updateWorkbook(Member member, Long workbookId, WorkbookReq workbookReq, MultipartFile thumbnailImage, MultipartFile workbookFile, MultipartFile answerFile) {
        // Validation
        /*
        TODO: 관리자 권한 확인 코드 반영 필요
         */
        Workbook workbook = workbookRepository.findById(workbookId).orElseThrow(() -> new WorkbookException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Business Logic: 이미지, 파일 등 등록 이후, Entity로 업데이트
        // 문제집 파일을 직접 올릴 경우
        if(workbookReq.getIsFile().equals(Boolean.TRUE)) {
            try {
                if(thumbnailImage != null && !thumbnailImage.isEmpty()) {
                    s3Client.delete(workbook.getThumbnailUrl());
                    String updateThumbnailImageUrl = s3Client.upload(thumbnailImage, "workbook/" + workbookReq.getType());
                    workbook.updateThumbnailUrl(updateThumbnailImageUrl);
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

                return new WorkbookRes(workbook, null);

            } catch (Exception e) {
                throw new WorkbookException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
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
                workbook.update(workbookReq);

                return new WorkbookRes(workbook, null);
            } catch (Exception e) {
                throw new WorkbookException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
            }
        }

        // Response
        return null;
    }

    @Transactional
    public Void deleteWorkbook(Member member, Long workbookId) {
        // Validation
        /*
        TODO: 관리자 권한 확인 코드 반영 필요
         */
        Workbook workbook = workbookRepository.findById(workbookId).orElseThrow(() -> new WorkbookException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Business Logic
        // 문제집 파일을 직접 올릴 경우
        workbookRepository.delete(workbook);
        if(workbook.getIsFile().equals(Boolean.TRUE)) {
            try {
                s3Client.delete(workbook.getThumbnailUrl());
                s3Client.delete(workbook.getWorkbookFileUrl());
                s3Client.delete(workbook.getAnswerFileUrl());
            } catch (Exception e) {
                throw new WorkbookException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
            }

        }
        // 문제를 별도로 등록하여, 참조 테이블로 엮어서 만드는 경우
        if(workbook.getIsFile().equals(Boolean.FALSE)) {
            try {
                s3Client.delete(workbook.getThumbnailUrl());
            } catch (Exception e) {
                throw new WorkbookException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
            }
        }

        // Response
        return null;
    }

    public WorkbookRes getOneWorkbook(Member member, Long workbookId) {
        // Validation: Controller에서 @Auth를 통해 인증된 사용자만 접근할 수 있도록 함.

        // Business Logic
        Workbook workbook = workbookRepository.findById(workbookId).orElseThrow(() -> new WorkbookException(ErrorCode.NOT_FOUND_EXCEPTION));
        WorkbookRes workbookRes = new WorkbookRes(workbook, null); // 추후, 문제 테이블을 만들어 문제별로 관리하게 된다면, 난이도 부분을 별도로 적용해야 함을 감안해 미리 생성 후 null로 바인딩.

        // Response
        return workbookRes;
    }

    public List<WorkbookCardRes> getMainWorkbookList(Member member) {
        // Validation: Controller에서 @Auth를 통해 인증된 사용자만 접근할 수 있도록 함.

        // Business Logic
        List<WorkbookCardRes> workbookCardResList = workbookRepository.findAllByCreatedAtDescAndLimit(4L).orElse(null);

        // Response
        return workbookCardResList;
    }

    public Slice<WorkbookCardRes> getWorkbookList(Member member, String subjectDetail, String type, Long cursor, Pageable pageable) {
        // Validation: Controller에서 @Auth를 통해 인증된 사용자만 접근할 수 있도록 함.

        // Business Logic
        Slice<WorkbookCardRes> workbookCardResSlice = workbookRepository.findWorkbookCardPageableBy(subjectDetail, type, cursor, pageable);

        // Response
        return workbookCardResSlice;
    }
}
