package com.certforge.service;

import com.certforge.domain.Question;
import com.certforge.domain.QuestionBankMetadata;
import com.certforge.i18n.Language;
import com.certforge.parser.MarkdownQuestionBankParser;
import com.certforge.parser.ParseFailure;
import com.certforge.parser.QuestionBankParseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QuestionBankService {
    private static final Logger log = LoggerFactory.getLogger(QuestionBankService.class);

    private final Map<String, Question> questionsById;
    private final List<Question> questions;
    private final QuestionBankParseResult parseResult;
    private final QuestionBankMetadata metadata;
    private final com.certforge.i18n.LanguageContext languageContext;

    public QuestionBankService(ResourceLoader resourceLoader,
                               MarkdownQuestionBankParser parser,
                               com.certforge.i18n.LanguageContext languageContext,
                               @Value("${certforge.question-bank:classpath:/question-bank/AWS_AIP-C01.md}") String sourceLocation) {
        this.languageContext = languageContext;
        Resource resource = resourceLoader.getResource(sourceLocation);
        if (!resource.exists()) {
            throw new IllegalStateException("Question bank not found: " + sourceLocation);
        }
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            this.parseResult = parser.parse(reader);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read question bank: " + sourceLocation, exception);
        }
        this.questions = parseResult.questions().stream()
                .sorted(Comparator.comparingInt(Question::questionNumber))
                .toList();
        this.questionsById = questions.stream().collect(Collectors.toUnmodifiableMap(Question::id, Function.identity()));
        if (questions.isEmpty()) {
            throw new IllegalStateException("Question bank contains no valid questions: " + sourceLocation);
        }

        this.metadata = new QuestionBankMetadata(
                "aws-aip-c01",
                "AWS Certified Generative AI Developer - Professional",
                "AIP-C01",
                "source",
                sourceLocation,
                questions.size());

        log.info("Question bank loaded");
        log.info("Parsed: {}, Failed: {}, Single choice: {}, Multiple choice: {}",
                questions.size(), parseResult.failures().size(), parseResult.singleChoiceCount(), parseResult.multipleChoiceCount());
        for (ParseFailure failure : parseResult.failures()) {
            log.warn("Question {} was not parsed: {} | source fragment: {}",
                    failure.questionNumber(), failure.reason(), failure.sourceFragment());
        }
    }

    public List<Question> all() {
        return questions;
    }

    public Optional<Question> findById(String id) {
        return Optional.ofNullable(questionsById.get(id));
    }

    public Question require(String id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("Unknown question: " + id));
    }

    public QuestionBankMetadata metadata() {
        return metadata;
    }

    public QuestionBankParseResult parseResult() {
        return parseResult;
    }

    public List<String> topics() {
        Language language = languageContext.current();
        return questions.stream().map(question -> question.localizedTopic(language)).distinct().sorted().toList();
    }
}
