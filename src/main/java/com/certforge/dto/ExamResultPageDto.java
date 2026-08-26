package com.certforge.dto;

import java.util.List;

public class ExamResultPageDto {
    private final int total;
    private final int correct;
    private final int incorrect;
    private final int unanswered;
    private final double percentage;
    private final long timeUsedSeconds;
    private final List<ExamAnswerReviewDto> reviews;

    public ExamResultPageDto(int total, int correct, int incorrect, int unanswered, double percentage,
                             long timeUsedSeconds, List<ExamAnswerReviewDto> reviews) {
        this.total = total;
        this.correct = correct;
        this.incorrect = incorrect;
        this.unanswered = unanswered;
        this.percentage = percentage;
        this.timeUsedSeconds = timeUsedSeconds;
        this.reviews = List.copyOf(reviews);
    }

    public int getTotal() { return total; }
    public int getCorrect() { return correct; }
    public int getIncorrect() { return incorrect; }
    public int getUnanswered() { return unanswered; }
    public double getPercentage() { return percentage; }
    public long getTimeUsedSeconds() { return timeUsedSeconds; }
    public List<ExamAnswerReviewDto> getReviews() { return reviews; }
}
