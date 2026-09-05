package com.certforge.service;

import com.certforge.domain.CommunityVote;
import com.certforge.domain.Question;
import com.certforge.domain.QuestionOption;
import com.certforge.dto.CheckResultDto;
import com.certforge.dto.CommunityVoteDto;
import com.certforge.dto.OptionViewDto;
import com.certforge.dto.QuestionViewDto;
import com.certforge.dto.StudyQuestionDto;
import com.certforge.i18n.Language;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class QuestionViewMapper {
    private final ProgressService progressService;
    private final com.certforge.i18n.LanguageContext languageContext;
    private final QuestionExplanationService questionExplanationService;
    private final ExplanationMarkdownRenderer explanationRenderer;

    public QuestionViewMapper(ProgressService progressService, com.certforge.i18n.LanguageContext languageContext,
                              QuestionExplanationService questionExplanationService,
                              ExplanationMarkdownRenderer explanationRenderer) {
        this.progressService = progressService;
        this.languageContext = languageContext;
        this.questionExplanationService = questionExplanationService;
        this.explanationRenderer = explanationRenderer;
    }

    public QuestionViewDto review(Question question, Set<String> selected, boolean revealAnswers) {
        return view(question, selected, revealAnswers, List.of(), languageContext.current());
    }

    public QuestionViewDto review(Question question, Set<String> selected, boolean revealAnswers,
                                 List<String> optionOrder) {
        return view(question, selected, revealAnswers, optionOrder, languageContext.current());
    }

    public StudyQuestionDto study(Question question) {
        return new StudyQuestionDto(view(question, Set.of(), true, List.of(), languageContext.current()),
                sorted(question.correctAnswers()), votes(question.communityVotes()), explanationHtml(question));
    }

    public CheckResultDto result(Question question, Set<String> selected, boolean correct) {
        return new CheckResultDto(correct, selected, sorted(question.correctAnswers()), votes(question.communityVotes()),
                explanationHtml(question));
    }

    public String explanationHtml(Question question) {
        String markdown = questionExplanationService.forQuestion(question.questionNumber(), languageContext.current());
        return explanationRenderer.render(markdown);
    }

    public QuestionViewDto view(Question question, Set<String> selected, boolean revealAnswers) {
        return view(question, selected, revealAnswers, List.of(), languageContext.current());
    }

    public QuestionViewDto view(Question question, Set<String> selected, boolean revealAnswers,
                                List<String> optionOrder) {
        return view(question, selected, revealAnswers, optionOrder, languageContext.current());
    }

    public QuestionViewDto view(Question question, Set<String> selected, boolean revealAnswers,
                                List<String> optionOrder, Language language) {
        boolean starred = progressService.isStarred(question.id());
        Map<String, com.certforge.domain.QuestionOption> optionsByLabel = question.localizedOptions(language).stream()
                .collect(Collectors.toMap(com.certforge.domain.QuestionOption::label, Function.identity()));
        List<com.certforge.domain.QuestionOption> orderedOptions = optionOrder == null || optionOrder.isEmpty()
                ? question.localizedOptions(language)
                : optionOrder.stream().map(optionsByLabel::get).filter(java.util.Objects::nonNull).toList();
        List<OptionViewDto> options = orderedOptions.stream()
                .map(option -> option(option, selected, revealAnswers ? question.correctAnswers() : Set.of()))
                .toList();
        return new QuestionViewDto(question.id(), question.questionNumber(), question.localizedTopic(language), question.text(language),
                options, question.multipleChoice(), question.shortAnswer(), starred);
    }

    public List<CommunityVoteDto> votes(List<CommunityVote> source) {
        return source.stream().map(vote -> new CommunityVoteDto(vote.answerCombination(), vote.percentage())).toList();
    }

    public List<String> sorted(Set<String> answers) {
        return answers.stream().sorted().toList();
    }

    private OptionViewDto option(QuestionOption option, Set<String> selected, Set<String> correctAnswers) {
        return new OptionViewDto(option.label(), option.text(), option.mostVoted(), selected.contains(option.label()),
                correctAnswers.contains(option.label()));
    }
}
