package com.certforge.domain;

import java.time.Instant;
import java.util.Set;

public record QuestionAttempt(
        String questionId,
        Set<String> selectedAnswers,
        boolean correct,
        boolean answered,
        Instant checkedAt,
        PracticeMode practiceMode) {

    public QuestionAttempt {
        selectedAnswers = Set.copyOf(selectedAnswers);
    }
}
