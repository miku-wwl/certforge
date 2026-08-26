package com.certforge.service;

import com.certforge.domain.PracticeMode;
import com.certforge.domain.PracticeSession;
import com.certforge.domain.Question;
import com.certforge.domain.QuestionAttempt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ExamService {
    private final QuestionBankService questionBankService;
    private final PracticeService practiceService;
    private final ProgressService progressService;

    public ExamService(QuestionBankService questionBankService, PracticeService practiceService,
                       ProgressService progressService) {
        this.questionBankService = questionBankService;
        this.practiceService = practiceService;
        this.progressService = progressService;
    }

    public ExamResult submit(PracticeSession session) {
        List<ExamAnswer> answers = new ArrayList<>();
        for (String questionId : session.questionIds()) {
            Question question = questionBankService.require(questionId);
            Set<String> selected = session.answerFor(questionId);
            QuestionAttempt attempt = practiceService.attempt(question, selected, PracticeMode.EXAM);
            progressService.recordAttempt(attempt);
            answers.add(new ExamAnswer(question, selected, attempt.answered(), attempt.correct()));
        }
        int correct = (int) answers.stream().filter(ExamAnswer::correct).count();
        int answered = (int) answers.stream().filter(ExamAnswer::answered).count();
        return new ExamResult(answers, correct, answered - correct, answers.size() - answered,
                answers.isEmpty() ? 0.0 : correct * 100.0 / answers.size(),
                Math.max(0, (System.currentTimeMillis() - session.startedAtMillis()) / 1000));
    }

    public record ExamAnswer(Question question, Set<String> selectedAnswers, boolean answered, boolean correct) {
    }

    public record ExamResult(List<ExamAnswer> answers, int correct, int incorrect, int unanswered,
                             double percentage, long timeUsedSeconds) {
    }
}
