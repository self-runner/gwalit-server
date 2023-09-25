package com.selfrunner.gwalit.domain.workbook.service;

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
}
