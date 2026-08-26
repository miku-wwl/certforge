package com.certforge.dto;

import java.util.Set;

public class WrongQuestionRowDto {
    private final QuestionViewDto question;
    private final int incorrectCount;
    private final Set<String> lastSelectedAnswers;
    private final String lastAttempt;
    private final boolean mastered;

    public WrongQuestionRowDto(QuestionViewDto question, int incorrectCount, Set<String> lastSelectedAnswers,
                               String lastAttempt, boolean mastered) {
        this.question = question;
        this.incorrectCount = incorrectCount;
        this.lastSelectedAnswers = Set.copyOf(lastSelectedAnswers);
        this.lastAttempt = lastAttempt;
        this.mastered = mastered;
    }

    public QuestionViewDto getQuestion() { return question; }
    public int getIncorrectCount() { return incorrectCount; }
    public Set<String> getLastSelectedAnswers() { return lastSelectedAnswers; }
    public String getLastAttempt() { return lastAttempt; }
    public boolean isMastered() { return mastered; }
}
