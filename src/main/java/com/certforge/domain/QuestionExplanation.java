package com.certforge.domain;

import com.certforge.i18n.Language;

public record QuestionExplanation(int questionNumber, String chineseText, String englishText) {
    public QuestionExplanation {
        chineseText = chineseText == null ? "" : chineseText.trim();
        englishText = englishText == null ? "" : englishText.trim();
    }

    public String text(Language language) {
        if (language == Language.EN && !englishText.isBlank()) {
            return englishText;
        }
        return !chineseText.isBlank() ? chineseText : englishText;
    }
}
