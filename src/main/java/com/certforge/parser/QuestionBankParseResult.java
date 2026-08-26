package com.certforge.parser;

import com.certforge.domain.Question;

import java.util.List;

public record QuestionBankParseResult(
        List<Question> questions,
        List<ParseFailure> failures,
        int detectedQuestionCount) {

    public QuestionBankParseResult {
        questions = List.copyOf(questions);
        failures = List.copyOf(failures);
    }

    public long singleChoiceCount() {
        return questions.stream().filter(question -> !question.multipleChoice()).count();
    }

    public long multipleChoiceCount() {
        return questions.stream().filter(Question::multipleChoice).count();
    }
}
