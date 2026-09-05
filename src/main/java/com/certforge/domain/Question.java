package com.certforge.domain;

import com.certforge.i18n.Language;

import java.util.List;
import java.util.Set;

public record Question(
        String id,
        int questionNumber,
        String topic,
        String questionText,
        List<QuestionOption> options,
        Set<String> correctAnswers,
        List<CommunityVote> communityVotes,
        String chineseQuestionText,
        List<QuestionOption> chineseOptions,
        String chineseTopic,
        String answerText,
        String chineseAnswerText) {

    public Question(String id, int questionNumber, String topic, String questionText,
                    List<QuestionOption> options, Set<String> correctAnswers,
                    List<CommunityVote> communityVotes) {
        this(id, questionNumber, topic, questionText, options, correctAnswers, communityVotes,
                questionText, options, topic, "", "");
    }

    public Question(String id, int questionNumber, String topic, String questionText,
                    List<QuestionOption> options, Set<String> correctAnswers,
                    List<CommunityVote> communityVotes, String chineseQuestionText,
                    List<QuestionOption> chineseOptions, String chineseTopic) {
        this(id, questionNumber, topic, questionText, options, correctAnswers, communityVotes,
                chineseQuestionText, chineseOptions, chineseTopic, "", "");
    }

    public Question {
        options = List.copyOf(options);
        correctAnswers = Set.copyOf(correctAnswers);
        communityVotes = List.copyOf(communityVotes);
        chineseQuestionText = chineseQuestionText == null || chineseQuestionText.isBlank()
                ? questionText : chineseQuestionText;
        chineseOptions = chineseOptions == null || chineseOptions.isEmpty()
                ? options : List.copyOf(chineseOptions);
        chineseTopic = chineseTopic == null || chineseTopic.isBlank() ? topic : chineseTopic;
        answerText = answerText == null ? "" : answerText.trim();
        chineseAnswerText = chineseAnswerText == null || chineseAnswerText.isBlank()
                ? answerText : chineseAnswerText.trim();
    }

    public boolean multipleChoice() {
        return correctAnswers.size() > 1;
    }

    public boolean shortAnswer() {
        return !answerText.isBlank() && options.isEmpty();
    }

    public String text(Language language) {
        return language == Language.ZH ? chineseQuestionText : questionText;
    }

    public List<QuestionOption> localizedOptions(Language language) {
        return language == Language.ZH ? chineseOptions : options;
    }

    public String answer(Language language) {
        return language == Language.ZH ? chineseAnswerText : answerText;
    }

    public String localizedTopic(Language language) {
        if (language == Language.ZH) {
            if (chineseTopic.equals(topic) && topic.regionMatches(true, 0, "Topic", 0, 5)) {
                return "主题" + topic.substring(5);
            }
            return chineseTopic;
        }
        return topic;
    }
}
