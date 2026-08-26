package com.certforge.repository;

import com.certforge.domain.persistence.AttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttemptRepository extends JpaRepository<AttemptEntity, Long> {
    List<AttemptEntity> findByQuestionIdOrderByCheckedAtDesc(String questionId);
}
