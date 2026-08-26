package com.certforge.domain;

public record QuestionBankMetadata(
        String id,
        String title,
        String code,
        String version,
        String sourceFile,
        int questionCount) {
}
