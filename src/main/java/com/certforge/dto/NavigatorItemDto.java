package com.certforge.dto;

public class NavigatorItemDto {
    private final int questionNumber;
    private final String questionId;
    private final String state;
    private final boolean current;
    private final boolean flagged;

    public NavigatorItemDto(int questionNumber, String questionId, String state, boolean current, boolean flagged) {
        this.questionNumber = questionNumber;
        this.questionId = questionId;
        this.state = state;
        this.current = current;
        this.flagged = flagged;
    }

    public int getQuestionNumber() { return questionNumber; }
    public String getQuestionId() { return questionId; }
    public String getState() { return state; }
    public boolean isCurrent() { return current; }
    public boolean isFlagged() { return flagged; }
}
