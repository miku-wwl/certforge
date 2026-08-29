package com.certforge.service;

import com.certforge.domain.PracticeMode;
import com.certforge.domain.Question;
import com.certforge.domain.persistence.AttemptEntity;
import com.certforge.domain.persistence.QuestionProgressEntity;
import com.certforge.repository.AttemptRepository;
import com.certforge.repository.QuestionProgressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AnswerImportService {
    private static final DateTimeFormatter EXPORT_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final QuestionBankService questionBankService;
    private final QuestionProgressRepository progressRepository;
    private final AttemptRepository attemptRepository;

    public AnswerImportService(QuestionBankService questionBankService,
                               QuestionProgressRepository progressRepository,
                               AttemptRepository attemptRepository) {
        this.questionBankService = questionBankService;
        this.progressRepository = progressRepository;
        this.attemptRepository = attemptRepository;
    }

    /**
     * Restores a complete export. The import is all-or-nothing and replaces the
     * current local attempts and progress records after the CSV is validated.
     */
    @Transactional
    public ImportResult importCsv(byte[] bytes) {
        ParsedImport parsed = parse(bytes);
        attemptRepository.deleteAllInBatch();
        progressRepository.deleteAllInBatch();

        int attempts = 0;
        for (Question question : questionBankService.all()) {
            List<ImportedRow> rows = parsed.rowsByQuestion().get(question.id());
            for (ImportedRow row : rows.stream()
                    .filter(imported -> imported.attemptNumber() != null)
                    .sorted(Comparator.comparingInt(ImportedRow::attemptNumber))
                    .toList()) {
                attemptRepository.save(new AttemptEntity(question.id(), encode(row.selectedAnswers()),
                        row.attemptResult() == Result.CORRECT, row.answered(), row.attemptTime(), row.practiceMode()));
                attempts++;
            }

            ImportedRow snapshot = rows.stream()
                    .max(Comparator.comparing(ImportedRow::snapshotOrder))
                    .orElseThrow(() -> error("题目没有可恢复的状态 / question has no restorable row"));
            ProgressSnapshot progress = snapshot.progressSnapshot();
            if (!progress.starred() && progress.answeredCount() == 0) {
                continue;
            }

            Instant lastAttemptAt = rows.stream()
                    .filter(row -> row.attemptNumber() != null && row.answered() && row.attemptTime() != null)
                    .max(Comparator.comparing(ImportedRow::snapshotOrder))
                    .map(ImportedRow::attemptTime)
                    .orElse(null);
            QuestionProgressEntity entity = new QuestionProgressEntity(question.id());
            entity.setStarred(progress.starred());
            entity.setHasAnswered(progress.answeredCount() > 0);
            entity.setLastCorrect(progress.latestResult() == Result.CORRECT);
            entity.setAttemptCount(progress.answeredCount());
            entity.setIncorrectCount(progress.incorrectCount());
            entity.setConsecutiveCorrect(progress.consecutiveCorrect());
            entity.setLastSelectedAnswers(encode(progress.latestAnswers()));
            entity.setLastAttemptAt(lastAttemptAt);
            progressRepository.save(entity);
        }
        return new ImportResult(parsed.rowsByQuestion().size(), attempts);
    }

    private ParsedImport parse(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw error("文件为空 / file is empty");
        }
        String csv = new String(bytes, StandardCharsets.UTF_8);
        if (!csv.isEmpty() && csv.charAt(0) == '\uFEFF') {
            csv = csv.substring(1);
        }
        List<List<String>> records = parseCsv(csv);
        if (records.isEmpty()) {
            throw error("文件为空 / file is empty");
        }
        Map<Column, Integer> columns = columns(records.get(0));
        Map<String, Question> questions = questionBankService.all().stream()
                .collect(Collectors.toUnmodifiableMap(Question::id, Function.identity()));
        Map<String, List<ImportedRow>> rowsByQuestion = new LinkedHashMap<>();
        for (int index = 1; index < records.size(); index++) {
            List<String> record = records.get(index);
            if (record.stream().allMatch(String::isBlank)) {
                continue;
            }
            int csvRow = index + 1;
            if (record.size() != columns.size()) {
                throw error("第 " + csvRow + " 行列数不正确 / row has an unexpected number of columns");
            }
            ImportedRow row = parseRow(record, columns, questions, csvRow);
            rowsByQuestion.computeIfAbsent(row.questionId(), ignored -> new ArrayList<>()).add(row);
        }

        Set<String> expected = questions.keySet();
        Set<String> actual = rowsByQuestion.keySet();
        Set<String> missing = new HashSet<>(expected);
        missing.removeAll(actual);
        Set<String> unknown = new HashSet<>(actual);
        unknown.removeAll(expected);
        if (!missing.isEmpty() || !unknown.isEmpty()) {
            throw error("CSV 必须包含当前题库的全部题目 / CSV must contain every current question; missing "
                    + missing.size() + ", unknown " + unknown.size());
        }
        for (Map.Entry<String, List<ImportedRow>> entry : rowsByQuestion.entrySet()) {
            validateQuestionRows(entry.getValue());
        }
        return new ParsedImport(rowsByQuestion);
    }

    private Map<Column, Integer> columns(List<String> header) {
        EnumMap<Column, Integer> columns = new EnumMap<>(Column.class);
        for (int index = 0; index < header.size(); index++) {
            String normalized = normalize(header.get(index));
            for (Column column : Column.values()) {
                if (column.aliases().contains(normalized)) {
                    if (columns.put(column, index) != null) {
                        throw error("CSV 表头重复 / duplicate header: " + header.get(index));
                    }
                    break;
                }
            }
        }
        List<Column> missing = List.of(Column.values()).stream()
                .filter(column -> !columns.containsKey(column)).toList();
        if (!missing.isEmpty()) {
            throw error("CSV 表头不完整 / incomplete CSV header: "
                    + missing.stream().map(Column::displayName).collect(Collectors.joining(", ")));
        }
        return columns;
    }

    private ImportedRow parseRow(List<String> record, Map<Column, Integer> columns,
                                 Map<String, Question> questions, int csvRow) {
        String questionId = value(record, columns, Column.QUESTION_ID);
        Question question = questions.get(questionId);
        if (question == null) {
            throw rowError(csvRow, "未知题目 ID / unknown question ID: " + questionId);
        }
        int questionNumber = parseInt(value(record, columns, Column.QUESTION_NUMBER), csvRow, Column.QUESTION_NUMBER);
        if (questionNumber != question.questionNumber()) {
            throw rowError(csvRow, "题号与题目 ID 不匹配 / question number does not match question ID");
        }
        Set<String> correctAnswers = parseAnswers(value(record, columns, Column.CORRECT_ANSWERS), question, csvRow,
                Column.CORRECT_ANSWERS);
        if (!correctAnswers.equals(new TreeSet<>(question.correctAnswers()))) {
            throw rowError(csvRow, "正确答案与当前题库不匹配 / correct answers do not match the current bank");
        }

        ProgressSnapshot progress = new ProgressSnapshot(
                parseBoolean(value(record, columns, Column.STARRED), csvRow, Column.STARRED),
                parseResult(value(record, columns, Column.LATEST_RESULT), csvRow, Column.LATEST_RESULT),
                parseAnswers(value(record, columns, Column.LATEST_ANSWERS), question, csvRow, Column.LATEST_ANSWERS),
                parseNonNegative(value(record, columns, Column.ANSWERED_COUNT), csvRow, Column.ANSWERED_COUNT),
                parseNonNegative(value(record, columns, Column.INCORRECT_COUNT), csvRow, Column.INCORRECT_COUNT),
                parseNonNegative(value(record, columns, Column.CONSECUTIVE_CORRECT), csvRow, Column.CONSECUTIVE_CORRECT));

        String attemptNumberValue = value(record, columns, Column.ATTEMPT_NUMBER);
        if (attemptNumberValue.isBlank()) {
            validateProgress(progress, csvRow, question);
            return new ImportedRow(questionId, null, Set.of(), false, Result.UNANSWERED, null, null, progress);
        }

        int attemptNumber = parseInt(attemptNumberValue, csvRow, Column.ATTEMPT_NUMBER);
        if (attemptNumber < 1) {
            throw rowError(csvRow, "答题次数必须大于 0 / attempt number must be positive");
        }
        boolean answered = parseBoolean(value(record, columns, Column.ANSWERED), csvRow, Column.ANSWERED);
        Result result = parseResult(value(record, columns, Column.ATTEMPT_RESULT), csvRow, Column.ATTEMPT_RESULT);
        Set<String> selectedAnswers = parseAnswers(value(record, columns, Column.SELECTED_ANSWERS), question, csvRow,
                Column.SELECTED_ANSWERS);
        Instant attemptTime = parseTime(value(record, columns, Column.ATTEMPT_TIME), csvRow);
        PracticeMode practiceMode = parsePracticeMode(value(record, columns, Column.PRACTICE_MODE), csvRow);
        if (answered && (result == Result.UNANSWERED || selectedAnswers.isEmpty())) {
            throw rowError(csvRow, "已作答记录缺少选择或结果 / answered attempt is missing a selection or result");
        }
        if (!answered && result != Result.UNANSWERED) {
            throw rowError(csvRow, "未作答记录的结果必须是未作答 / unanswered attempt has an invalid result");
        }
        if (attemptTime == null || practiceMode == null) {
            throw rowError(csvRow, "答题记录缺少时间或答题模式 / attempt is missing time or practice mode");
        }
        validateProgress(progress, csvRow, question);
        return new ImportedRow(questionId, attemptNumber, selectedAnswers, answered, result, attemptTime, practiceMode,
                progress);
    }

    private void validateQuestionRows(List<ImportedRow> rows) {
        Set<Integer> attemptNumbers = rows.stream().map(ImportedRow::attemptNumber)
                .filter(number -> number != null).collect(Collectors.toSet());
        if (attemptNumbers.size() != rows.stream().filter(row -> row.attemptNumber() != null).count()) {
            throw error("同一题的答题次数重复 / duplicate attempt number for a question");
        }
        if (rows.stream().anyMatch(row -> row.attemptNumber() == null)
                && rows.stream().anyMatch(row -> row.attemptNumber() != null)) {
            throw error("同一题不能同时包含空答题行和答题记录 / a question mixes empty and attempt rows");
        }
    }

    private void validateProgress(ProgressSnapshot progress, int csvRow, Question question) {
        if (progress.incorrectCount() > progress.answeredCount()
                || progress.consecutiveCorrect() > progress.answeredCount()) {
            throw rowError(csvRow, "答题统计不一致 / progress counts are inconsistent");
        }
        if (progress.answeredCount() == 0) {
            if (progress.latestResult() != Result.UNANSWERED || !progress.latestAnswers().isEmpty()) {
                throw rowError(csvRow, "未答题的最近状态不一致 / unanswered progress is inconsistent");
            }
        } else if (progress.latestResult() == Result.UNANSWERED || progress.latestAnswers().isEmpty()) {
            throw rowError(csvRow, "已答题的最近状态不完整 / answered progress is incomplete");
        }
        Set<String> allowed = question.options().stream().map(option -> option.label().toUpperCase(Locale.ROOT))
                .collect(Collectors.toSet());
        if (!allowed.containsAll(progress.latestAnswers())) {
            throw rowError(csvRow, "最近选择包含无效选项 / latest answers contain an invalid option");
        }
    }

    private static String value(List<String> record, Map<Column, Integer> columns, Column column) {
        return record.get(columns.get(column)).trim();
    }

    private static int parseInt(String value, int row, Column column) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw rowError(row, column.displayName() + " 不是有效数字 / is not a valid number");
        }
    }

    private static int parseNonNegative(String value, int row, Column column) {
        int result = parseInt(value, row, column);
        if (result < 0) {
            throw rowError(row, column.displayName() + " 不能为负数 / cannot be negative");
        }
        return result;
    }

    private static boolean parseBoolean(String value, int row, Column column) {
        String normalized = normalize(value);
        if (Set.of("yes", "true", "1", "是", "已收藏", "starred").contains(normalized)) return true;
        if (Set.of("no", "false", "0", "否", "未收藏", "not starred").contains(normalized)) return false;
        throw rowError(row, column.displayName() + " 不是有效的是/否值 / is not a valid yes/no value");
    }

    private static Result parseResult(String value, int row, Column column) {
        String normalized = normalize(value);
        if (normalized.isBlank() || Set.of("unanswered", "未作答", "未答").contains(normalized)) {
            return Result.UNANSWERED;
        }
        if (Set.of("correct", "正确", "right").contains(normalized)) return Result.CORRECT;
        if (Set.of("incorrect", "错误", "wrong").contains(normalized)) return Result.INCORRECT;
        throw rowError(row, column.displayName() + " 不是有效结果 / is not a valid result");
    }

    private static PracticeMode parsePracticeMode(String value, int row) {
        String normalized = normalize(value);
        if (Set.of("review", "instant practice", "即时答题").contains(normalized)) return PracticeMode.REVIEW;
        if (Set.of("exam", "practice exam", "模拟考试").contains(normalized)) return PracticeMode.EXAM;
        if (normalized.isBlank()) return null;
        throw rowError(row, "答题模式无效 / invalid practice mode");
    }

    private static Instant parseTime(String value, int row) {
        if (value.isBlank()) return null;
        try {
            return LocalDateTime.parse(value, EXPORT_TIME).atZone(ZoneId.systemDefault()).toInstant();
        } catch (DateTimeParseException ignored) {
            try {
                return Instant.parse(value);
            } catch (DateTimeParseException exception) {
                throw rowError(row, "答题时间无效 / invalid attempt time");
            }
        }
    }

    private static Set<String> parseAnswers(String value, Question question, int row, Column column) {
        if (value == null || value.isBlank()) return Set.of();
        Set<String> allowed = question.options().stream().map(option -> option.label().toUpperCase(Locale.ROOT))
                .collect(Collectors.toSet());
        Set<String> answers = new TreeSet<>();
        for (String token : value.trim().split("[,，;；|/\\s]+")) {
            if (token.isBlank()) continue;
            String answer = token.trim().toUpperCase(Locale.ROOT);
            if (!allowed.contains(answer)) {
                throw rowError(row, column.displayName() + " 包含无效选项 / contains an invalid option: " + token);
            }
            answers.add(answer);
        }
        return answers;
    }

    private static String encode(Set<String> answers) {
        return answers == null ? "" : answers.stream().sorted().collect(Collectors.joining(","));
    }

    private static String normalize(String value) {
        return value == null ? "" : value.replace("\uFEFF", "").trim().toLowerCase(Locale.ROOT);
    }

    private static List<List<String>> parseCsv(String input) {
        List<List<String>> records = new ArrayList<>();
        List<String> record = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;
        boolean afterClosingQuote = false;
        for (int index = 0; index < input.length(); index++) {
            char current = input.charAt(index);
            if (inQuotes) {
                if (current == '"') {
                    if (index + 1 < input.length() && input.charAt(index + 1) == '"') {
                        field.append('"');
                        index++;
                    } else {
                        inQuotes = false;
                        afterClosingQuote = true;
                    }
                } else {
                    field.append(current);
                }
                continue;
            }
            if (afterClosingQuote) {
                if (current == ',') {
                    record.add(field.toString());
                    field.setLength(0);
                    afterClosingQuote = false;
                } else if (current == '\r' || current == '\n') {
                    record.add(field.toString());
                    field.setLength(0);
                    records.add(record);
                    record = new ArrayList<>();
                    afterClosingQuote = false;
                    if (current == '\r' && index + 1 < input.length() && input.charAt(index + 1) == '\n') index++;
                } else if (!Character.isWhitespace(current)) {
                    throw error("CSV 引号格式错误 / malformed CSV quotes");
                }
                continue;
            }
            if (current == '"' && field.length() == 0) {
                inQuotes = true;
            } else if (current == ',') {
                record.add(field.toString());
                field.setLength(0);
            } else if (current == '\r' || current == '\n') {
                record.add(field.toString());
                field.setLength(0);
                records.add(record);
                record = new ArrayList<>();
                if (current == '\r' && index + 1 < input.length() && input.charAt(index + 1) == '\n') index++;
            } else {
                field.append(current);
            }
        }
        if (inQuotes) throw error("CSV 引号未闭合 / unterminated CSV quote");
        if (!record.isEmpty() || field.length() > 0 || afterClosingQuote) {
            record.add(field.toString());
            records.add(record);
        }
        return records;
    }

    private static ImportException rowError(int row, String message) {
        return error("第 " + row + " 行：" + message + " / row " + row + ": " + message);
    }

    private static ImportException error(String message) {
        return new ImportException(message);
    }

    public record ImportResult(int questionCount, int attemptCount) {
    }

    public static final class ImportException extends RuntimeException {
        public ImportException(String message) {
            super(message);
        }
    }

    private record ParsedImport(Map<String, List<ImportedRow>> rowsByQuestion) {
    }

    private record ImportedRow(String questionId, Integer attemptNumber, Set<String> selectedAnswers,
                               boolean answered, Result attemptResult, Instant attemptTime,
                               PracticeMode practiceMode, ProgressSnapshot progressSnapshot) {
        private Integer snapshotOrder() {
            return attemptNumber == null ? 0 : attemptNumber;
        }
    }

    private record ProgressSnapshot(boolean starred, Result latestResult, Set<String> latestAnswers,
                                    int answeredCount, int incorrectCount, int consecutiveCorrect) {
    }

    private enum Result { CORRECT, INCORRECT, UNANSWERED }

    private enum Column {
        QUESTION_NUMBER("题号", "question no.", "question no"),
        QUESTION_ID("题目ID", "question id"),
        TOPIC("主题", "topic"),
        QUESTION_TYPE("题型", "question type"),
        QUESTION("题干", "question"),
        OPTIONS("选项", "options"),
        CORRECT_ANSWERS("正确答案", "correct answers"),
        ATTEMPT_ID("答题记录ID", "attempt id"),
        ATTEMPT_NUMBER("本题第几次答题", "attempt no.", "attempt no"),
        SELECTED_ANSWERS("本次选择", "selected answers"),
        ATTEMPT_RESULT("本次结果", "attempt result"),
        ANSWERED("是否作答", "answered"),
        PRACTICE_MODE("答题模式", "practice mode"),
        ATTEMPT_TIME("答题时间", "attempt time"),
        LATEST_RESULT("最近结果", "latest result"),
        LATEST_ANSWERS("最近选择", "latest answers"),
        MASTERY("掌握状态", "mastery status"),
        ANSWERED_COUNT("有效作答次数", "answered count"),
        INCORRECT_COUNT("累计错题次数", "incorrect count"),
        CONSECUTIVE_CORRECT("连续答对次数", "consecutive correct"),
        STARRED("星标", "starred");

        private final Set<String> aliases;

        Column(String... aliases) {
            this.aliases = Arrays.stream(aliases).map(AnswerImportService::normalize).collect(Collectors.toUnmodifiableSet());
        }

        Set<String> aliases() {
            return aliases;
        }

        String displayName() {
            return aliases.iterator().next();
        }
    }
}
