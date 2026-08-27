package com.certforge.service;

import com.certforge.domain.Question;
import com.certforge.domain.QuestionExplanation;
import com.certforge.i18n.Language;
import com.certforge.parser.MarkdownQuestionExplanationParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

@Service
public class QuestionExplanationService {
    private final Map<Integer, QuestionExplanation> explanations;

    public QuestionExplanationService(ResourcePatternResolver resourceResolver,
                                      QuestionBankService questionBankService,
                                      @Value("${certforge.question-explanations:classpath*:/question-bank/AWS_AIP-C01_explanations_*.md}") String sourcePattern) {
        MarkdownQuestionExplanationParser parser = new MarkdownQuestionExplanationParser();
        Map<Integer, QuestionExplanation> loaded = new LinkedHashMap<>();
        Resource[] resources;
        try {
            resources = resourceResolver.getResources(sourcePattern);
            Arrays.sort(resources, java.util.Comparator.comparing(Resource::getFilename,
                    java.util.Comparator.nullsLast(String::compareTo)));
            for (Resource resource : resources) {
                try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                    parser.parse(reader).forEach((questionNumber, explanation) -> {
                        if (loaded.putIfAbsent(questionNumber, explanation) != null) {
                            throw new IllegalStateException("Duplicate explanation for question " + questionNumber);
                        }
                    });
                }
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load question explanations: " + sourcePattern, exception);
        }

        Set<Integer> expected = questionBankService.all().stream()
                .map(Question::questionNumber)
                .collect(Collectors.toCollection(TreeSet::new));
        Set<Integer> missing = new TreeSet<>(expected);
        missing.removeAll(loaded.keySet());
        Set<Integer> unexpected = new TreeSet<>(loaded.keySet());
        unexpected.removeAll(expected);
        if (resources.length == 0 || !missing.isEmpty() || !unexpected.isEmpty()) {
            throw new IllegalStateException("Question explanations must cover the complete question bank. Missing: "
                    + missing + ", unexpected: " + unexpected);
        }
        questionBankService.all().forEach(question -> validateDepth(question, loaded.get(question.questionNumber())));
        this.explanations = Map.copyOf(loaded);
    }

    public String forQuestion(int questionNumber, Language language) {
        QuestionExplanation explanation = explanations.get(questionNumber);
        if (explanation == null) {
            throw new IllegalArgumentException("No explanation for question " + questionNumber);
        }
        return explanation.text(language);
    }

    public int count() {
        return explanations.size();
    }

    private static void validateDepth(Question question, QuestionExplanation explanation) {
        requireSections(question.questionNumber(), explanation.chineseText(), List.of(
                "#### 考点背景", "#### 场景比喻", "#### AWS 服务角色",
                "#### 正确答案与推理", "#### 逐项排除", "#### 解题方法"));
        requireSections(question.questionNumber(), explanation.englishText(), List.of(
                "#### Exam focus and background", "#### Analogy", "#### AWS service roles",
                "#### Correct answer and reasoning", "#### Option-by-option elimination", "#### Exam strategy"));
        if (!explanation.chineseText().contains("| AWS 服务 |")
                || !explanation.englishText().contains("| AWS service |")) {
            throw new IllegalStateException("Question " + question.questionNumber() + " is missing an AWS service role table");
        }
        if (explanation.chineseText().length() < 300 || wordCount(explanation.englishText()) < 120) {
            throw new IllegalStateException("Question " + question.questionNumber() + " explanation is too shallow");
        }
        for (String label : question.options().stream().map(option -> option.label()).toList()) {
            Pattern option = Pattern.compile("(?m)^-\\s+(?:\\*\\*)?" + Pattern.quote(label)
                    + "(?:\\s*[:：.、-]\\s*)?(?:\\*\\*)?");
            if (!option.matcher(explanation.chineseText()).find() || !option.matcher(explanation.englishText()).find()) {
                throw new IllegalStateException("Question " + question.questionNumber()
                        + " explanation does not analyze option " + label + " in both languages");
            }
        }
    }

    private static void requireSections(int questionNumber, String text, List<String> headings) {
        List<String> missing = headings.stream().filter(heading -> !text.contains(heading)).toList();
        if (!missing.isEmpty()) {
            throw new IllegalStateException("Question " + questionNumber + " explanation is missing sections: " + missing);
        }
    }

    private static long wordCount(String text) {
        return Pattern.compile("[A-Za-z0-9][A-Za-z0-9'-]*").matcher(text).results().count();
    }
}
