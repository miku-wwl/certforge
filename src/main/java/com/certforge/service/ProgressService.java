package com.certforge.service;

import com.certforge.domain.QuestionAttempt;
import com.certforge.domain.StudyProgress;
import com.certforge.domain.persistence.AttemptEntity;
import com.certforge.domain.persistence.QuestionProgressEntity;
import com.certforge.repository.AttemptRepository;
import com.certforge.repository.QuestionProgressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class ProgressService {
    private static final DateTimeFormatter DISPLAY_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.systemDefault());

    private final QuestionProgressRepository progressRepository;
    private final AttemptRepository attemptRepository;

    public ProgressService(QuestionProgressRepository progressRepository, AttemptRepository attemptRepository) {
        this.progressRepository = progressRepository;
        this.attemptRepository = attemptRepository;
    }

    @Transactional
    public QuestionAttempt recordAttempt(QuestionAttempt attempt) {
        Instant checkedAt = attempt.checkedAt() == null ? Instant.now() : attempt.checkedAt();
        AttemptEntity entity = new AttemptEntity(attempt.questionId(), encode(attempt.selectedAnswers()),
                attempt.correct(), attempt.answered(), checkedAt, attempt.practiceMode());
        attemptRepository.save(entity);

        if (!attempt.answered()) {
            return new QuestionAttempt(attempt.questionId(), attempt.selectedAnswers(), false, false,
                    checkedAt, attempt.practiceMode());
        }

        QuestionProgressEntity progress = progressRepository.findById(attempt.questionId())
                .orElseGet(() -> new QuestionProgressEntity(attempt.questionId()));
        progress.setHasAnswered(true);
        progress.setLastCorrect(attempt.correct());
        progress.setAttemptCount(progress.getAttemptCount() + 1);
        progress.setIncorrectCount(progress.getIncorrectCount() + (attempt.correct() ? 0 : 1));
        progress.setConsecutiveCorrect(attempt.correct() ? progress.getConsecutiveCorrect() + 1 : 0);
        progress.setLastSelectedAnswers(encode(attempt.selectedAnswers()));
        progress.setLastAttemptAt(checkedAt);
        progressRepository.save(progress);
        return new QuestionAttempt(attempt.questionId(), attempt.selectedAnswers(), attempt.correct(), true,
                checkedAt, attempt.practiceMode());
    }

    @Transactional
    public boolean toggleStar(String questionId) {
        QuestionProgressEntity progress = progressRepository.findById(questionId)
                .orElseGet(() -> new QuestionProgressEntity(questionId));
        progress.setStarred(!progress.isStarred());
        progressRepository.save(progress);
        return progress.isStarred();
    }

    @Transactional(readOnly = true)
    public boolean isStarred(String questionId) {
        return progressRepository.findById(questionId).map(QuestionProgressEntity::isStarred).orElse(false);
    }

    @Transactional(readOnly = true)
    public Map<String, QuestionProgressEntity> progressByQuestion() {
        return progressRepository.findAll().stream()
                .collect(Collectors.toUnmodifiableMap(QuestionProgressEntity::getQuestionId, progress -> progress));
    }

    @Transactional(readOnly = true)
    public StudyProgress summary(int totalQuestions) {
        List<QuestionProgressEntity> rows = progressRepository.findAll();
        int attempted = (int) rows.stream().filter(QuestionProgressEntity::isHasAnswered).count();
        int correct = (int) rows.stream().filter(row -> row.isHasAnswered() && row.isLastCorrect()).count();
        int incorrect = (int) rows.stream().filter(row -> row.isHasAnswered() && !row.isLastCorrect()).count();
        int starred = (int) rows.stream().filter(QuestionProgressEntity::isStarred).count();
        int everWrong = (int) rows.stream().filter(row -> row.getIncorrectCount() > 0).count();
        BigDecimal accuracy = attempted == 0 ? BigDecimal.ZERO
                : BigDecimal.valueOf(correct * 100.0 / attempted).setScale(1, RoundingMode.HALF_UP);
        return new StudyProgress(totalQuestions, attempted, correct, incorrect,
                Math.max(0, totalQuestions - attempted), starred, everWrong, accuracy);
    }

    @Transactional(readOnly = true)
    public List<WrongQuestionSummary> wrongQuestions() {
        return progressRepository.findAll().stream()
                .filter(progress -> progress.getIncorrectCount() > 0)
                .sorted(Comparator.comparing(QuestionProgressEntity::getLastAttemptAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(progress -> new WrongQuestionSummary(progress.getQuestionId(), progress.getIncorrectCount(),
                        decode(progress.getLastSelectedAnswers()), format(progress.getLastAttemptAt()),
                        progress.getConsecutiveCorrect() >= 2))
                .toList();
    }

    @Transactional(readOnly = true)
    public MasteryStatus masteryStatus(String questionId) {
        return progressRepository.findById(questionId)
                .filter(QuestionProgressEntity::isHasAnswered)
                .map(progress -> progress.getConsecutiveCorrect() >= 2 ? MasteryStatus.MASTERED : MasteryStatus.LEARNING)
                .orElse(MasteryStatus.NEW);
    }

    private static String encode(Set<String> answers) {
        return answers.stream().sorted().collect(Collectors.joining(","));
    }

    private static Set<String> decode(String answers) {
        if (answers == null || answers.isBlank()) {
            return Set.of();
        }
        return new TreeSet<>(Set.of(answers.split(",")));
    }

    private static String format(Instant time) {
        return time == null ? "-" : DISPLAY_TIME.format(time);
    }

    public enum MasteryStatus { NEW, LEARNING, MASTERED }

    public record WrongQuestionSummary(String questionId, int incorrectCount, Set<String> lastSelectedAnswers,
                                       String lastAttempt, boolean mastered) {
    }
}
