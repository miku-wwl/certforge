package com.certforge.parser;

import com.certforge.domain.QuestionExplanation;
import com.certforge.i18n.Language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownQuestionExplanationParser {
    private static final Pattern QUESTION_HEADING = Pattern.compile("^##\\s+Question\\s+(\\d+)\\s*$", Pattern.CASE_INSENSITIVE);
    private static final Pattern LANGUAGE_HEADING = Pattern.compile("^###\\s+(中文|English)\\s*$", Pattern.CASE_INSENSITIVE);

    public Map<Integer, QuestionExplanation> parse(Reader source) throws IOException {
        List<String> lines = new BufferedReader(source).lines().toList();
        Map<Integer, QuestionExplanation> explanations = new LinkedHashMap<>();
        Draft current = null;

        for (String rawLine : lines) {
            String line = rawLine.trim();
            Matcher questionMatcher = QUESTION_HEADING.matcher(line);
            if (questionMatcher.matches()) {
                if (current != null) {
                    add(explanations, current);
                }
                current = new Draft(Integer.parseInt(questionMatcher.group(1)));
                continue;
            }
            if (current == null) {
                continue;
            }
            Matcher languageMatcher = LANGUAGE_HEADING.matcher(line);
            if (languageMatcher.matches()) {
                current.language = "中文".equalsIgnoreCase(languageMatcher.group(1)) ? Language.ZH : Language.EN;
                continue;
            }
            if (!line.isBlank() && !line.equals("---") && current.language != null) {
                current.lines.get(current.language).add(line);
            }
        }
        if (current != null) {
            add(explanations, current);
        }
        return Map.copyOf(explanations);
    }

    private static void add(Map<Integer, QuestionExplanation> explanations, Draft draft) {
        QuestionExplanation explanation = new QuestionExplanation(draft.questionNumber,
                String.join("\n", draft.lines.get(Language.ZH)),
                String.join("\n", draft.lines.get(Language.EN)));
        if (explanation.chineseText().isBlank() || explanation.englishText().isBlank()) {
            throw new IllegalArgumentException("Question " + draft.questionNumber + " is missing a bilingual explanation");
        }
        if (explanations.putIfAbsent(draft.questionNumber, explanation) != null) {
            throw new IllegalArgumentException("Duplicate explanation for question " + draft.questionNumber);
        }
    }

    private static final class Draft {
        private final int questionNumber;
        private final Map<Language, List<String>> lines = new java.util.EnumMap<>(Language.class);
        private Language language;

        private Draft(int questionNumber) {
            this.questionNumber = questionNumber;
            lines.put(Language.ZH, new ArrayList<>());
            lines.put(Language.EN, new ArrayList<>());
        }
    }
}
