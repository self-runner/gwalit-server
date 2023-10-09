package com.selfrunner.gwalit.domain.workbook.service;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.workbook.dto.request.PostWorkbookReq;
import com.selfrunner.gwalit.domain.workbook.dto.request.PutWorkbookReq;
import com.selfrunner.gwalit.domain.workbook.dto.response.*;
import com.selfrunner.gwalit.domain.workbook.repository.WorkbookRepository;
import com.selfrunner.gwalit.global.util.aws.S3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorkbookService {

    private final WorkbookRepository workbookRepository;
    private final S3Client s3Client;



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
}
