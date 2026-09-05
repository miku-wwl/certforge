package com.certforge;

import com.certforge.domain.Question;
import com.certforge.parser.MarkdownQuestionBankParser;
import com.certforge.parser.QuestionBankParseResult;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MarkdownQuestionBankParserTest {
    @Test
    void parsesSingleMultipleChineseLabelsVotesAndReportsMalformedQuestion() throws Exception {
        var resource = getClass().getResourceAsStream("/question-bank/parser-fixture.md");
        QuestionBankParseResult result = new MarkdownQuestionBankParser().parse(
                new InputStreamReader(resource, StandardCharsets.UTF_8));

        assertEquals(4, result.detectedQuestionCount());
        assertEquals(3, result.questions().size());
        assertEquals(1, result.failures().size());
        assertEquals(3, result.singleChoiceCount() + result.multipleChoiceCount());
        assertEquals(1, result.singleChoiceCount());
        assertEquals(2, result.multipleChoiceCount());

        Question english = result.questions().get(0);
        assertEquals(1, english.questionNumber());
        assertEquals("Topic English", english.topic());
        assertEquals(Set.of("C"), english.correctAnswers());
        assertEquals(2, english.communityVotes().size());
        assertTrue(english.options().stream().filter(option -> option.label().equals("D")).findFirst().orElseThrow().mostVoted());

        Question chinese = result.questions().get(1);
        assertEquals(Set.of("A", "D", "F"), chinese.correctAnswers());
        assertEquals(6, chinese.options().size());
        assertTrue(chinese.options().get(5).mostVoted());

        Question noVotes = result.questions().get(2);
        assertEquals(Set.of("A", "B"), noVotes.correctAnswers());
        assertTrue(noVotes.communityVotes().isEmpty());
        assertFalse(result.failures().get(0).sourceFragment().isBlank());
    }

    @Test
    void parsesBilingualQuestionSectionsIntoBothLanguages() throws Exception {
        var resource = getClass().getResourceAsStream("/question-bank/bilingual-fixture.md");
        Question question = new MarkdownQuestionBankParser().parse(
                new InputStreamReader(resource, StandardCharsets.UTF_8)).questions().get(0);

        assertEquals("This is the English question.", question.questionText());
        assertEquals("这是中文题干。", question.chineseQuestionText());
        assertEquals("English option B", question.options().get(1).text());
        assertEquals("中文选项 B", question.chineseOptions().get(1).text());
        assertTrue(question.options().get(1).mostVoted());
        assertTrue(question.chineseOptions().get(1).mostVoted());
        assertEquals(Set.of("B"), question.correctAnswers());
        assertEquals(75, question.communityVotes().get(0).percentage());
    }

    @Test
    void parsesSreShortAnswerBlocksAndKeepsReferenceAnswer() throws Exception {
        String source = """
                # 01 — Git 内部原理
                ## Q1 — .git 目录
                ### Q1.1 — 基础定义
                **问题：** `.git` 目录保存什么？
                **参考答案：**
                保存仓库元数据和对象。
                ---
                ### Q1.2 — 内部机制
                **问题：** 什么是 object database？
                **参考答案：**
                Git 使用内容寻址对象库。
                """;

        QuestionBankParseResult result = new MarkdownQuestionBankParser().parse(new StringReader(source));
        assertEquals(2, result.detectedQuestionCount());
        assertEquals(2, result.questions().size());
        assertTrue(result.questions().stream().allMatch(Question::shortAnswer));
        assertEquals("保存仓库元数据和对象。", result.questions().get(0).answerText());
        assertTrue(result.questions().get(0).questionText().contains("基础定义"));
        assertTrue(result.questions().get(0).options().isEmpty());
        assertEquals(2, result.shortAnswerCount());
        assertEquals(0, result.singleChoiceCount());
    }
}
