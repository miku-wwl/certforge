package com.certforge.service;

import com.certforge.domain.PracticeMode;
import com.certforge.domain.Question;
import com.certforge.domain.QuestionOption;
import com.certforge.domain.persistence.AttemptEntity;
import com.certforge.domain.persistence.QuestionProgressEntity;
import com.certforge.i18n.Language;
import com.certforge.repository.AttemptRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class AnswerExportService {
    private static final DateTimeFormatter EXPORT_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    private final QuestionBankService questionBankService;
    private final ProgressService progressService;
    private final AttemptRepository attemptRepository;

    public AnswerExportService(QuestionBankService questionBankService, ProgressService progressService,
                               AttemptRepository attemptRepository) {
        this.questionBankService = questionBankService;
        this.progressService = progressService;
        this.attemptRepository = attemptRepository;
    }

    public byte[] export(Language language) {
        Map<String, QuestionProgressEntity> progressByQuestion = progressService.progressByQuestion();
        Map<String, List<AttemptEntity>> attemptsByQuestion = attemptRepository.findAll().stream()
                .sorted(Comparator.comparing(AttemptEntity::getCheckedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.groupingBy(AttemptEntity::getQuestionId));

        StringBuilder csv = new StringBuilder("\uFEFF");
        appendRow(csv, headers(language));
        for (Question question : questionBankService.all()) {
            QuestionProgressEntity progress = progressByQuestion.get(question.id());
            List<AttemptEntity> attempts = attemptsByQuestion.getOrDefault(question.id(), List.of());
            if (attempts.isEmpty()) {
                appendRow(csv, row(question, progress, null, 0, language));
                continue;
            }
            for (int index = 0; index < attempts.size(); index++) {
                appendRow(csv, row(question, progress, attempts.get(index), attempts.size() - index, language));
            }
        }
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String[] headers(Language language) {
        if (language == Language.EN) {
            return new String[]{"Question No.", "Question ID", "Topic", "Question Type", "Question",
                    "Options", "Correct Answers", "Attempt ID", "Attempt No.", "Selected Answers",
                    "Attempt Result", "Answered", "Practice Mode", "Attempt Time", "Latest Result",
                    "Latest Answers", "Mastery Status", "Answered Count", "Incorrect Count",
                    "Consecutive Correct", "Starred"};
        }
        return new String[]{"题号", "题目ID", "主题", "题型", "题干", "选项", "正确答案", "答题记录ID",
                "本题第几次答题", "本次选择", "本次结果", "是否作答", "答题模式", "答题时间", "最近结果",
                "最近选择", "掌握状态", "有效作答次数", "累计错题次数", "连续答对次数", "星标"};
    }

    private String[] row(Question question, QuestionProgressEntity progress, AttemptEntity attempt,
                         int attemptNumber, Language language) {
        boolean english = language == Language.EN;
        String latestResult = latestResult(progress, english);
        String mastery = mastery(progress, english);
        String selectedAnswers = attempt == null ? "" : displayAnswers(attempt.getSelectedAnswers());
        String attemptResult = attempt == null ? unanswered(english) : attemptResult(attempt, english);
        String answered = attempt == null || !attempt.isAnswered() ? no(english) : yes(english);
        String practiceMode = attempt == null ? "" : practiceMode(attempt.getPracticeMode(), english);
        String attemptId = attempt == null || attempt.getId() == null ? "" : String.valueOf(attempt.getId());
        String time = attempt == null ? "" : format(attempt.getCheckedAt());
        String latestAnswers = progress == null ? "" : displayAnswers(progress.getLastSelectedAnswers());
        String options = question.localizedOptions(language).stream()
                .map(option -> option.label() + ". " + option.text())
                .collect(Collectors.joining(" | "));

        return new String[]{
                String.valueOf(question.questionNumber()),
                question.id(),
                question.localizedTopic(language),
                questionType(question, english),
                question.text(language),
                options,
                displayAnswers(String.join(",", new TreeSet<>(question.correctAnswers()))),
                attemptId,
                attempt == null ? "" : String.valueOf(attemptNumber),
                selectedAnswers,
                attemptResult,
                answered,
                practiceMode,
                time,
                latestResult,
                latestAnswers,
                mastery,
                progress == null ? "0" : String.valueOf(progress.getAttemptCount()),
                progress == null ? "0" : String.valueOf(progress.getIncorrectCount()),
                progress == null ? "0" : String.valueOf(progress.getConsecutiveCorrect()),
                progress != null && progress.isStarred() ? yes(english) : no(english)
        };
    }

    private static String questionType(Question question, boolean english) {
        if (english) return question.multipleChoice() ? "Multiple choice" : "Single choice";
        return question.multipleChoice() ? "多选题" : "单选题";
    }

    private static String latestResult(QuestionProgressEntity progress, boolean english) {
        if (progress == null || !progress.isHasAnswered()) return unanswered(english);
        return progress.isLastCorrect() ? correct(english) : incorrect(english);
    }

    private static String mastery(QuestionProgressEntity progress, boolean english) {
        if (progress == null || !progress.isHasAnswered()) return english ? "New" : "未开始";
        return progress.getConsecutiveCorrect() >= 2
                ? (english ? "Mastered" : "已掌握")
                : (english ? "Learning" : "学习中");
    }

    private static String attemptResult(AttemptEntity attempt, boolean english) {
        if (!attempt.isAnswered()) return unanswered(english);
        return attempt.isCorrect() ? correct(english) : incorrect(english);
    }

    private static String practiceMode(PracticeMode mode, boolean english) {
        if (mode == null) return "";
        if (english) return mode == PracticeMode.EXAM ? "Practice exam" : "Instant practice";
        return mode == PracticeMode.EXAM ? "模拟考试" : "即时答题";
    }

    private static String correct(boolean english) { return english ? "Correct" : "正确"; }
    private static String incorrect(boolean english) { return english ? "Incorrect" : "错误"; }
    private static String unanswered(boolean english) { return english ? "Unanswered" : "未作答"; }
    private static String yes(boolean english) { return english ? "Yes" : "是"; }
    private static String no(boolean english) { return english ? "No" : "否"; }

    private static String displayAnswers(String answers) {
        if (answers == null || answers.isBlank()) return "";
        return String.join(", ", answers.split(","));
    }

    private static String format(Instant time) {
        return time == null ? "" : EXPORT_TIME.format(time);
    }

    private static void appendRow(StringBuilder csv, String[] values) {
        for (int index = 0; index < values.length; index++) {
            if (index > 0) csv.append(',');
            String value = values[index] == null ? "" : values[index];
            boolean quoted = value.indexOf(',') >= 0 || value.indexOf('"') >= 0
                    || value.indexOf('\n') >= 0 || value.indexOf('\r') >= 0;
            if (quoted) csv.append('"');
            csv.append(value.replace("\"", "\"\""));
            if (quoted) csv.append('"');
        }
        csv.append("\r\n");
    }
}
