package com.selfrunner.gwalit.domain.question.repository;

import com.selfrunner.gwalit.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionRepositoryCustom {
}
