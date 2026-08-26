package com.certforge.repository;

import com.certforge.domain.persistence.QuestionProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionProgressRepository extends JpaRepository<QuestionProgressEntity, String> {
}
