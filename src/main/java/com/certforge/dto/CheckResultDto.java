package com.certforge.dto;

import java.util.List;
import java.util.Set;

public class CheckResultDto {
    private final boolean correct;
    private final Set<String> selectedAnswers;
    private final List<String> correctAnswers;
    private final List<CommunityVoteDto> communityVotes;
    private final String aiExplanationHtml;

    public CheckResultDto(boolean correct, Set<String> selectedAnswers, List<String> correctAnswers,
                          List<CommunityVoteDto> communityVotes, String aiExplanationHtml) {
        this.correct = correct;
        this.selectedAnswers = Set.copyOf(selectedAnswers);
        this.correctAnswers = List.copyOf(correctAnswers);
        this.communityVotes = List.copyOf(communityVotes);
        this.aiExplanationHtml = aiExplanationHtml;
    }

    public boolean isCorrect() { return correct; }
    public Set<String> getSelectedAnswers() { return selectedAnswers; }
    public List<String> getCorrectAnswers() { return correctAnswers; }
    public List<CommunityVoteDto> getCommunityVotes() { return communityVotes; }
    public String getAiExplanationHtml() { return aiExplanationHtml; }
    public boolean isHasVotes() { return !communityVotes.isEmpty(); }
}
