package com.certforge.service;

import com.certforge.domain.PracticeMode;
import com.certforge.domain.Question;
import com.certforge.domain.QuestionAttempt;
import com.certforge.domain.persistence.QuestionProgressEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class PracticeService {
    private final QuestionBankService questionBankService;
    private final ProgressService progressService;

    public PracticeService(QuestionBankService questionBankService, ProgressService progressService) {
        this.questionBankService = questionBankService;
        this.progressService = progressService;
    }

    public boolean grade(Question question, Collection<String> selectedAnswers) {
        if (question.shortAnswer()) {
            return false;
        }
        return normalize(selectedAnswers).equals(new TreeSet<>(question.correctAnswers()));
    }

    public Set<String> normalize(Collection<String> selectedAnswers) {
        if (selectedAnswers == null) {
            return Set.of();
        }
        return selectedAnswers.stream()
                .filter(value -> value != null && !value.isBlank())
                .map(value -> value.trim().toUpperCase(Locale.ROOT))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public QuestionAttempt attempt(Question question, Collection<String> selectedAnswers, PracticeMode mode) {
        Set<String> selected = normalize(selectedAnswers);
        return new QuestionAttempt(question.id(), selected, !selected.isEmpty() && grade(question, selected),
                !selected.isEmpty(), Instant.now(), mode);
    }

    public List<Question> select(SelectionCriteria criteria) {
        Map<String, QuestionProgressEntity> progress = progressService.progressByQuestion();
        List<Question> candidates = questionBankService.all().stream()
                .filter(question -> matches(question, criteria, progress.get(question.id())))
                .sorted(Comparator.comparingInt(Question::questionNumber))
                .collect(Collectors.toCollection(java.util.ArrayList::new));
        if (criteria.randomCount() != null && criteria.randomCount() > 0) {
            java.util.Collections.shuffle(candidates);
            if (candidates.size() > criteria.randomCount()) {
                candidates = new java.util.ArrayList<>(candidates.subList(0, criteria.randomCount()));
            }
            candidates.sort(Comparator.comparingInt(Question::questionNumber));
        }
        if (criteria.shuffleQuestions()) {
            java.util.Collections.shuffle(candidates);
        }
        return candidates;
    }

    private boolean matches(Question question, SelectionCriteria criteria, QuestionProgressEntity progress) {
        if (criteria.questionNumbers() != null && !criteria.questionNumbers().contains(question.questionNumber())) {
            return false;
        }
        if (criteria.topic() != null && !criteria.topic().isBlank()
                && !criteria.topic().equals(question.topic())
                && !criteria.topic().equals(question.chineseTopic())
                && !criteria.topic().equals(question.localizedTopic(com.certforge.i18n.Language.ZH))) {
            return false;
        }
        if (criteria.type() == SelectionType.SINGLE && (question.multipleChoice() || question.shortAnswer())) {
            return false;
        }
        if (criteria.type() == SelectionType.MULTIPLE && !question.multipleChoice()) {
            return false;
        }
        if (criteria.type() == SelectionType.SHORT_ANSWER && !question.shortAnswer()) {
            return false;
        }
        if (criteria.starredOnly() && (progress == null || !progress.isStarred())) {
            return false;
        }
        if (criteria.wrongOnly() && (progress == null || progress.getIncorrectCount() == 0)) {
            return false;
        }
        if (criteria.unansweredOnly() && (progress != null && progress.isHasAnswered())) {
            return false;
        }
        if (criteria.search() != null && !criteria.search().isBlank()) {
            String needle = criteria.search().toLowerCase(Locale.ROOT);
            boolean match = question.questionText().toLowerCase(Locale.ROOT).contains(needle)
                    || question.chineseQuestionText().toLowerCase(Locale.ROOT).contains(needle)
                    || question.options().stream().anyMatch(option -> option.text().toLowerCase(Locale.ROOT).contains(needle))
                    || question.chineseOptions().stream().anyMatch(option -> option.text().toLowerCase(Locale.ROOT).contains(needle));
            if (!match) {
                return false;
            }
        }
        return true;
    }

    public static Set<Integer> parseRange(String range) {
        if (range == null || range.isBlank()) {
            return null;
        }
        Set<Integer> numbers = new LinkedHashSet<>();
        for (String part : range.split(",")) {
            String[] bounds = part.trim().split("-");
            try {
                int start = Integer.parseInt(bounds[0].trim());
                int end = bounds.length > 1 ? Integer.parseInt(bounds[1].trim()) : start;
                if (start < 1 || end < start || end - start > 10000) {
                    return Set.of();
                }
                for (int number = start; number <= end; number++) {
                    numbers.add(number);
                }
            } catch (NumberFormatException exception) {
                return Set.of();
            }
        }
        return numbers;
    }

    public enum SelectionType { ALL, SINGLE, MULTIPLE, SHORT_ANSWER }

    public record SelectionCriteria(Set<Integer> questionNumbers, String search, String topic, SelectionType type,
                                    boolean starredOnly, boolean wrongOnly, boolean unansweredOnly,
                                    Integer randomCount, boolean shuffleQuestions) {
        public static SelectionCriteria all() {
            return new SelectionCriteria(null, null, null, SelectionType.ALL, false, false, false, null, false);
        }
    }
}
