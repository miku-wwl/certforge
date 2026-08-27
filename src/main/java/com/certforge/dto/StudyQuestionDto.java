package com.certforge.dto;

import java.util.List;

public class StudyQuestionDto {
    private final QuestionViewDto question;
    private final List<String> correctAnswers;
    private final List<CommunityVoteDto> communityVotes;
    private final String aiExplanationHtml;

    public StudyQuestionDto(QuestionViewDto question, List<String> correctAnswers,
                            List<CommunityVoteDto> communityVotes, String aiExplanationHtml) {
        this.question = question;
        this.correctAnswers = List.copyOf(correctAnswers);
        this.communityVotes = List.copyOf(communityVotes);
        this.aiExplanationHtml = aiExplanationHtml;
    }

    public QuestionViewDto getQuestion() { return question; }
    public List<String> getCorrectAnswers() { return correctAnswers; }
    public List<CommunityVoteDto> getCommunityVotes() { return communityVotes; }
    public String getAiExplanationHtml() { return aiExplanationHtml; }
    public boolean isHasVotes() { return !communityVotes.isEmpty(); }
}
