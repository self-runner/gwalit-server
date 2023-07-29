package com.selfrunner.gwalit.domain.homework.service;

import com.selfrunner.gwalit.domain.homework.repository.HomeworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HomeworkService {

    private final HomeworkRepository homeworkRepository;
}
