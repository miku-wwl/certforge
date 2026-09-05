package com.certforge.parser;

import com.certforge.domain.CommunityVote;
import com.certforge.domain.Question;
import com.certforge.domain.QuestionOption;
import com.certforge.i18n.Language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses the question-bank Markdown format without relying on fixed line numbers.
 * A malformed question becomes an explicit ParseFailure and never disappears silently.
 */
public class MarkdownQuestionBankParser {
    private static final Pattern QUESTION_HEADING = Pattern.compile(
            "^##\\s+(Question|问题)\\s+(\\d+)\\s*(?:-\\s*(.*))?$", Pattern.CASE_INSENSITIVE);
    private static final Pattern LANGUAGE_HEADING = Pattern.compile(
            "^###\\s+(中文|Chinese|English|英文)\\s*$", Pattern.CASE_INSENSITIVE);
    private static final Pattern OPTION = Pattern.compile(
            "^\\s*-\\s+\\*\\*([A-F])\\.\\*\\*\\s*(.*?)\\s*$|^\\s*-\\s+\\*?([A-F])[\\.)]\\*?\\s+(.*?)\\s*$",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern ANSWER = Pattern.compile(
            "(?i)(?:\\*\\*)?\\s*(Correct Answer|正确答案)(?:\\s*/\\s*(?:Correct Answer|正确答案))?\\s*(?:\\*\\*)?\\s*[:：]\\s*(?:\\*\\*)?\\s*`?\\s*([A-F\\s,，、]+?)\\s*`?\\s*$");
    private static final Pattern VOTE = Pattern.compile(
            "^\\s*-?\\s*([A-F]{1,6})\\s*[\\(（\\[]\\s*(\\d{1,3})\\s*%\\s*[\\)）\\]]\\s*$",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern MOST_VOTED = Pattern.compile(
            "\\s*\\*{0,2}(?:\\(Most Voted\\)|（最高票）|（得票最高）|\\(最高票\\)|\\(得票最高\\))\\*{0,2}\\s*", Pattern.CASE_INSENSITIVE);
    private static final Pattern SRE_TOPIC_HEADING = Pattern.compile("^#\\s+(\\d{2})\\s+[—-]\\s+(.+)$");
    private static final Pattern SRE_PARENT_HEADING = Pattern.compile("^##\\s+Q(\\d+)\\s+[—-]\\s+(.+)$");
    private static final Pattern SRE_SUB_HEADING = Pattern.compile("^###\\s+Q(\\d+)\\.(\\d+)\\s+[—-]\\s+(.+)$");

    public QuestionBankParseResult parse(Reader source) throws IOException {
        List<String> lines = new BufferedReader(source).lines().toList();
        if (lines.stream().anyMatch(line -> line.trim().startsWith("**问题：**"))) {
            return parseShortAnswerBank(lines);
        }
        return parseChoiceBank(lines);
    }

    private QuestionBankParseResult parseChoiceBank(List<String> lines) {
        List<QuestionBlock> blocks = new ArrayList<>();
        QuestionBlock current = null;

        for (int index = 0; index < lines.size(); index++) {
            Matcher heading = QUESTION_HEADING.matcher(lines.get(index).trim());
            if (heading.matches()) {
                if (current != null) {
                    blocks.add(current);
                }
                current = new QuestionBlock(heading.group(2), heading.group(3), index,
                        "问题".equalsIgnoreCase(heading.group(1)) ? Language.ZH : Language.EN);
            } else if (current != null) {
                current.lines.add(lines.get(index));
            }
        }
        if (current != null) {
            blocks.add(current);
        }

        List<Question> questions = new ArrayList<>();
        List<ParseFailure> failures = new ArrayList<>();
        for (QuestionBlock block : blocks) {
            try {
                questions.add(parseBlock(block));
            } catch (ParseException exception) {
                failures.add(new ParseFailure(block.numberAsInt(), exception.getMessage(), fragment(block)));
            }
        }
        return new QuestionBankParseResult(questions, failures, blocks.size());
    }

    private QuestionBankParseResult parseShortAnswerBank(List<String> lines) {
        List<ShortAnswerBlock> blocks = new ArrayList<>();
        ShortAnswerBlock current = null;
        String courseTopic = "SRE 基础知识";
        String parentTopic = "";
        int sequence = 0;

        for (String rawLine : lines) {
            String line = rawLine.trim();
            Matcher topicMatcher = SRE_TOPIC_HEADING.matcher(line);
            if (topicMatcher.matches()) {
                courseTopic = topicMatcher.group(1) + " — " + topicMatcher.group(2).trim();
                continue;
            }
            Matcher parentMatcher = SRE_PARENT_HEADING.matcher(line);
            if (parentMatcher.matches()) {
                parentTopic = "Q" + parentMatcher.group(1) + " — " + parentMatcher.group(2).trim();
                continue;
            }
            Matcher subMatcher = SRE_SUB_HEADING.matcher(line);
            if (subMatcher.matches()) {
                if (current != null) {
                    blocks.add(current);
                }
                sequence++;
                current = new ShortAnswerBlock(sequence,
                        courseTopic + " / " + parentTopic,
                        subMatcher.group(3).trim());
                continue;
            }
            if (current != null) {
                current.lines.add(rawLine);
            }
        }
        if (current != null) {
            blocks.add(current);
        }

        List<Question> questions = new ArrayList<>();
        List<ParseFailure> failures = new ArrayList<>();
        for (ShortAnswerBlock block : blocks) {
            try {
                questions.add(parseShortAnswerBlock(block));
            } catch (ParseException exception) {
                failures.add(new ParseFailure(block.number, exception.getMessage(), fragment(block.lines)));
            }
        }
        return new QuestionBankParseResult(questions, failures, blocks.size());
    }

    private Question parseShortAnswerBlock(ShortAnswerBlock block) {
        List<String> questionLines = new ArrayList<>();
        List<String> answerLines = new ArrayList<>();
        boolean readingQuestion = false;
        boolean readingAnswer = false;
        for (String rawLine : block.lines) {
            String line = rawLine.trim();
            if (line.startsWith("**问题：**")) {
                readingQuestion = true;
                readingAnswer = false;
                String inlineQuestion = line.substring("**问题：**".length()).trim();
                if (!inlineQuestion.isBlank()) {
                    questionLines.add(inlineQuestion);
                }
                continue;
            }
            if (line.startsWith("**参考答案：**")) {
                readingQuestion = false;
                readingAnswer = true;
                String inlineAnswer = line.substring("**参考答案：**".length()).trim();
                if (!inlineAnswer.isBlank()) {
                    answerLines.add(inlineAnswer);
                }
                continue;
            }
            if (line.equals("---")) {
                continue;
            }
            if (readingQuestion && !line.isBlank()) {
                questionLines.add(line);
            } else if (readingAnswer && !line.isBlank()) {
                answerLines.add(line);
            }
        }
        String questionText = String.join("\n", questionLines).trim();
        String answerText = String.join("\n", answerLines).trim();
        if (questionText.isBlank()) {
            throw new ParseException("question text is empty");
        }
        if (answerText.isBlank()) {
            throw new ParseException("reference answer is empty");
        }
        String topic = block.topic.isBlank() ? "SRE 基础知识" : block.topic;
        return new Question("sre-foundations-q-" + block.number, block.number, topic,
                "【" + block.subtopic + "】\n" + questionText,
                List.of(), Set.of(), List.of(),
                "【" + block.subtopic + "】\n" + questionText, List.of(), topic,
                answerText, answerText);
    }

    private Question parseBlock(QuestionBlock block) {
        int questionNumber = block.numberAsInt();
        String topic = block.topic == null || block.topic.isBlank() ? "General" : block.topic.trim();
        Map<Language, Map<String, OptionDraft>> optionsByLanguage = new java.util.EnumMap<>(Language.class);
        Map<Language, List<String>> questionLinesByLanguage = new java.util.EnumMap<>(Language.class);
        for (Language language : Language.values()) {
            optionsByLanguage.put(language, new LinkedHashMap<>());
            questionLinesByLanguage.put(language, new ArrayList<>());
        }
        List<CommunityVote> votes = new ArrayList<>();
        Set<String> correctAnswers = new LinkedHashSet<>();
        boolean inVotes = false;
        Language activeLanguage = block.defaultLanguage;
        String currentOption = null;

        for (String rawLine : block.lines) {
            String line = rawLine.trim();
            Matcher languageMatcher = LANGUAGE_HEADING.matcher(line);
            if (languageMatcher.matches()) {
                activeLanguage = languageMatcher.group(1).equalsIgnoreCase("中文")
                        || languageMatcher.group(1).equalsIgnoreCase("Chinese")
                        || languageMatcher.group(1).equalsIgnoreCase("英文") ? Language.ZH : Language.EN;
                currentOption = null;
                continue;
            }
            Matcher answerMatcher = ANSWER.matcher(line);
            if (answerMatcher.matches()) {
                correctAnswers = normalizeAnswers(answerMatcher.group(2));
                currentOption = null;
                continue;
            }
            if (isVoteHeading(line)) {
                inVotes = true;
                currentOption = null;
                continue;
            }
            if (inVotes) {
                Matcher voteMatcher = VOTE.matcher(line);
                if (voteMatcher.matches()) {
                    votes.add(new CommunityVote(voteMatcher.group(1).toUpperCase(Locale.ROOT),
                            Integer.parseInt(voteMatcher.group(2))));
                }
                continue;
            }

            Matcher optionMatcher = OPTION.matcher(rawLine);
            if (optionMatcher.matches()) {
                String label = (optionMatcher.group(1) != null ? optionMatcher.group(1) : optionMatcher.group(3))
                        .toUpperCase(Locale.ROOT);
                String optionText = optionMatcher.group(2) != null ? optionMatcher.group(2) : optionMatcher.group(4);
                boolean mostVoted = MOST_VOTED.matcher(optionText).find();
                optionText = MOST_VOTED.matcher(optionText).replaceAll("").trim();
                optionsByLanguage.get(activeLanguage).put(label, new OptionDraft(optionText, mostVoted));
                currentOption = label;
                continue;
            }

            if (currentOption != null && !line.isBlank() && !line.startsWith("---")) {
                OptionDraft draft = optionsByLanguage.get(activeLanguage).get(currentOption);
                if (draft != null) {
                    optionsByLanguage.get(activeLanguage).put(currentOption,
                            new OptionDraft(draft.text + " " + line, draft.mostVoted));
                }
            } else if (!line.isBlank() && !line.startsWith(">") && !line.startsWith("#")) {
                questionLinesByLanguage.get(activeLanguage).add(line);
            }
        }

        String questionText = textFor(questionLinesByLanguage, Language.EN, Language.ZH);
        String chineseQuestionText = textFor(questionLinesByLanguage, Language.ZH, Language.EN);
        Map<String, OptionDraft> englishOptions = optionsFor(optionsByLanguage, Language.EN, Language.ZH);
        Map<String, OptionDraft> chineseOptions = optionsFor(optionsByLanguage, Language.ZH, Language.EN);
        if (questionText.isBlank()) {
            throw new ParseException("question text is empty");
        }
        if (englishOptions.size() < 2) {
            throw new ParseException("fewer than two options were found");
        }
        if (correctAnswers.isEmpty()) {
            throw new ParseException("correct answer is missing or empty");
        }
        if (!englishOptions.keySet().containsAll(correctAnswers)) {
            throw new ParseException("correct answer references an option that was not parsed: " + correctAnswers);
        }

        List<QuestionOption> parsedOptions = englishOptions.entrySet().stream()
                .map(entry -> new QuestionOption(entry.getKey(), entry.getValue().text, entry.getValue().mostVoted))
                .toList();
        List<QuestionOption> parsedChineseOptions = chineseOptions.entrySet().stream()
                .map(entry -> new QuestionOption(entry.getKey(), entry.getValue().text, entry.getValue().mostVoted))
                .toList();
        return new Question("aip-c01-q-" + questionNumber, questionNumber, topic, questionText,
                parsedOptions, correctAnswers, votes, chineseQuestionText, parsedChineseOptions, topic);
    }

    private static String textFor(Map<Language, List<String>> source, Language preferred, Language fallback) {
        List<String> preferredLines = source.get(preferred);
        if (preferredLines != null && !preferredLines.isEmpty()) {
            return String.join("\n", preferredLines).trim();
        }
        return String.join("\n", source.get(fallback)).trim();
    }

    private static Map<String, OptionDraft> optionsFor(Map<Language, Map<String, OptionDraft>> source,
                                                        Language preferred, Language fallback) {
        Map<String, OptionDraft> preferredOptions = source.get(preferred);
        return preferredOptions != null && !preferredOptions.isEmpty()
                ? preferredOptions : source.get(fallback);
    }

    private static Set<String> normalizeAnswers(String raw) {
        Set<String> answers = new LinkedHashSet<>();
        for (char character : raw.toUpperCase(Locale.ROOT).toCharArray()) {
            if (character >= 'A' && character <= 'F') {
                answers.add(String.valueOf(character));
            }
        }
        return answers;
    }

    private static boolean isVoteHeading(String line) {
        String normalized = line.replace("*", "").replace(":", "").replace("：", "").trim();
        return normalized.equalsIgnoreCase("Community vote distribution")
                || normalized.equals("社区投票分布")
                || normalized.toLowerCase(Locale.ROOT).contains("community vote distribution")
                || normalized.contains("社区投票分布");
    }

    private static String fragment(QuestionBlock block) {
        String body = String.join("\n", block.lines).trim();
        return body.length() <= 500 ? body : body.substring(0, 500) + "...";
    }

    private static String fragment(List<String> lines) {
        String body = String.join("\n", lines).trim();
        return body.length() <= 500 ? body : body.substring(0, 500) + "...";
    }

    private record OptionDraft(String text, boolean mostVoted) {
    }

    private static final class QuestionBlock {
        private final String number;
        private final String topic;
        private final int headingLine;
        private final Language defaultLanguage;
        private final List<String> lines = new ArrayList<>();

        private QuestionBlock(String number, String topic, int headingLine, Language defaultLanguage) {
            this.number = number;
            this.topic = topic;
            this.headingLine = headingLine;
            this.defaultLanguage = defaultLanguage;
        }

        private int numberAsInt() {
            try {
                return Integer.parseInt(number);
            } catch (NumberFormatException exception) {
                throw new ParseException("invalid question number at source line " + (headingLine + 1));
            }
        }
    }

    private static final class ShortAnswerBlock {
        private final int number;
        private final String topic;
        private final String subtopic;
        private final List<String> lines = new ArrayList<>();

        private ShortAnswerBlock(int number, String topic, String subtopic) {
            this.number = number;
            this.topic = topic;
            this.subtopic = subtopic;
        }
    }

    private static final class ParseException extends RuntimeException {
        private ParseException(String message) {
            super(message);
        }
    }
}
