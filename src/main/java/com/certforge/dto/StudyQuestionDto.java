package com.certforge.dto;

import java.util.List;

public class StudyQuestionDto {
    private final QuestionViewDto question;
    private final List<String> correctAnswers;
    private final List<CommunityVoteDto> communityVotes;

    public StudyQuestionDto(QuestionViewDto question, List<String> correctAnswers,
                            List<CommunityVoteDto> communityVotes) {
        this.question = question;
        this.correctAnswers = List.copyOf(correctAnswers);
        this.communityVotes = List.copyOf(communityVotes);
    }

    public QuestionViewDto getQuestion() { return question; }
    public List<String> getCorrectAnswers() { return correctAnswers; }
    public List<CommunityVoteDto> getCommunityVotes() { return communityVotes; }
    public boolean isHasVotes() { return !communityVotes.isEmpty(); }
}
