package com.certforge.dto;

import java.util.List;

public class QuestionViewDto {
    private final String id;
    private final int questionNumber;
    private final String topic;
    private final String questionText;
    private final List<OptionViewDto> options;
    private final boolean multipleChoice;
    private final boolean starred;

    public QuestionViewDto(String id, int questionNumber, String topic, String questionText,
                            List<OptionViewDto> options, boolean multipleChoice, boolean starred) {
        this.id = id;
        this.questionNumber = questionNumber;
        this.topic = topic;
        this.questionText = questionText;
        this.options = List.copyOf(options);
        this.multipleChoice = multipleChoice;
        this.starred = starred;
    }

    public String getId() { return id; }
    public int getQuestionNumber() { return questionNumber; }
    public String getTopic() { return topic; }
    public String getQuestionText() { return questionText; }
    public List<OptionViewDto> getOptions() { return options; }
    public boolean isMultipleChoice() { return multipleChoice; }
    public boolean isStarred() { return starred; }
}
