package com.selfrunner.gwalit.domain.workbook.service;

import com.selfrunner.gwalit.domain.banner.entity.Banner;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
import com.selfrunner.gwalit.domain.workbook.dto.request.PostProblemReq;
import com.selfrunner.gwalit.domain.workbook.dto.response.PostProblemRes;
import com.selfrunner.gwalit.domain.workbook.entity.Problem;
import com.selfrunner.gwalit.domain.workbook.exception.WorkbookException;
import com.selfrunner.gwalit.domain.workbook.repository.ProblemRepository;
import com.selfrunner.gwalit.domain.workbook.repository.WorkbookAndProblemRepository;
import com.selfrunner.gwalit.domain.workbook.repository.WorkbookRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
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
    private final WorkbookAndProblemRepository workbookAndProblemRepository;
    private final ProblemRepository problemRepository;
    private final S3Client s3Client;

    @Transactional
    public PostProblemRes registerProblem(Member member, PostProblemReq postProblemReq, MultipartFile problemFile, MultipartFile solveFile) {
        // Validation
        /*
        TODO: 관리자 권한 확인 코드 반영 필요
         */

        // Business Logic
        try {
            String problemUrl = s3Client.upload(problemFile, "problem/problem");
            String solveUrl = s3Client.upload(solveFile, "problem/solve");
            Problem problem =postProblemReq.toEntity(problemUrl, solveUrl);
            problemRepository.save(problem);

            // Response
            return PostProblemRes.toDto(problem);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }
}
