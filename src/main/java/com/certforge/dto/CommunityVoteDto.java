package com.certforge.dto;

public class CommunityVoteDto {
    private final String answerCombination;
    private final int percentage;

    public CommunityVoteDto(String answerCombination, int percentage) {
        this.answerCombination = answerCombination;
        this.percentage = percentage;
    }

    public String getAnswerCombination() { return answerCombination; }
    public int getPercentage() { return percentage; }
}
