package com.certforge.domain.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "question_progress")
public class QuestionProgressEntity {
    @Id
    private String questionId;
    private boolean starred;
    private boolean lastCorrect;
    private boolean hasAnswered;
    private int attemptCount;
    private int incorrectCount;
    private int consecutiveCorrect;
    private String lastSelectedAnswers;
    private Instant lastAttemptAt;

    protected QuestionProgressEntity() {
    }

    public QuestionProgressEntity(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionId() { return questionId; }
    public boolean isStarred() { return starred; }
    public void setStarred(boolean starred) { this.starred = starred; }
    public boolean isLastCorrect() { return lastCorrect; }
    public void setLastCorrect(boolean lastCorrect) { this.lastCorrect = lastCorrect; }
    public boolean isHasAnswered() { return hasAnswered; }
    public void setHasAnswered(boolean hasAnswered) { this.hasAnswered = hasAnswered; }
    public int getAttemptCount() { return attemptCount; }
    public void setAttemptCount(int attemptCount) { this.attemptCount = attemptCount; }
    public int getIncorrectCount() { return incorrectCount; }
    public void setIncorrectCount(int incorrectCount) { this.incorrectCount = incorrectCount; }
    public int getConsecutiveCorrect() { return consecutiveCorrect; }
    public void setConsecutiveCorrect(int consecutiveCorrect) { this.consecutiveCorrect = consecutiveCorrect; }
    public String getLastSelectedAnswers() { return lastSelectedAnswers; }
    public void setLastSelectedAnswers(String lastSelectedAnswers) { this.lastSelectedAnswers = lastSelectedAnswers; }
    public Instant getLastAttemptAt() { return lastAttemptAt; }
    public void setLastAttemptAt(Instant lastAttemptAt) { this.lastAttemptAt = lastAttemptAt; }
}
