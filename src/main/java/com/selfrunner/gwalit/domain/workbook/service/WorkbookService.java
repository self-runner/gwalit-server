package com.selfrunner.gwalit.domain.workbook.service;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.workbook.dto.request.PostProblemReq;
import com.selfrunner.gwalit.domain.workbook.repository.ProblemRepository;
import com.selfrunner.gwalit.domain.workbook.repository.WorkbookAndProblemRepository;
import com.selfrunner.gwalit.domain.workbook.repository.WorkbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorkbookService {

    private final WorkbookRepository workbookRepository;
    private final WorkbookAndProblemRepository workbookAndProblemRepository;
    private final ProblemRepository problemRepository;

    @Transactional
    public Void register(Member member, PostProblemReq postProblemReq) {
        // Validation

        // Business Logic

        // Response
        return null;
    }
}
