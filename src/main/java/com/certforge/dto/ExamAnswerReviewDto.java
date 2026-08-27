package com.certforge.dto;

import java.util.List;
import java.util.Set;

public class ExamAnswerReviewDto {
    private final QuestionViewDto question;
    private final Set<String> selectedAnswers;
    private final List<String> correctAnswers;
    private final boolean correct;
    private final boolean answered;
    private final List<CommunityVoteDto> communityVotes;
    private final String aiExplanationHtml;

    public ExamAnswerReviewDto(QuestionViewDto question, Set<String> selectedAnswers, List<String> correctAnswers,
                               boolean correct, boolean answered, List<CommunityVoteDto> communityVotes,
                               String aiExplanationHtml) {
        this.question = question;
        this.selectedAnswers = Set.copyOf(selectedAnswers);
        this.correctAnswers = List.copyOf(correctAnswers);
        this.correct = correct;
        this.answered = answered;
        this.communityVotes = List.copyOf(communityVotes);
        this.aiExplanationHtml = aiExplanationHtml;
    }

    public QuestionViewDto getQuestion() { return question; }
    public Set<String> getSelectedAnswers() { return selectedAnswers; }
    public List<String> getCorrectAnswers() { return correctAnswers; }
    public boolean isCorrect() { return correct; }
    public boolean isAnswered() { return answered; }
    public List<CommunityVoteDto> getCommunityVotes() { return communityVotes; }
    public String getAiExplanationHtml() { return aiExplanationHtml; }
    public boolean isHasVotes() { return !communityVotes.isEmpty(); }
}
