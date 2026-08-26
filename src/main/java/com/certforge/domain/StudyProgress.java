package com.certforge.domain;

import java.math.BigDecimal;

public record StudyProgress(
        int total,
        int attempted,
        int correct,
        int incorrect,
        int unanswered,
        int starred,
        int everWrong,
        BigDecimal accuracy) {
}
