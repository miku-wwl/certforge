package com.certforge.dto;

public class OptionViewDto {
    private final String label;
    private final String text;
    private final boolean mostVoted;
    private final boolean selected;
    private final boolean correct;

    public OptionViewDto(String label, String text, boolean mostVoted, boolean selected, boolean correct) {
        this.label = label;
        this.text = text;
        this.mostVoted = mostVoted;
        this.selected = selected;
        this.correct = correct;
    }

    public String getLabel() { return label; }
    public String getText() { return text; }
    public boolean isMostVoted() { return mostVoted; }
    public boolean isSelected() { return selected; }
    public boolean isCorrect() { return correct; }
}
