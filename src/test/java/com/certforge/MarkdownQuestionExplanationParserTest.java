package com.certforge;

import com.certforge.parser.MarkdownQuestionExplanationParser;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MarkdownQuestionExplanationParserTest {
    @Test
    void parsesBilingualExplanation() throws Exception {
        String markdown = """
                ## Question 9
                ### 中文
                中文解析。
                ### English
                English explanation.
                ---
                """;

        var explanation = new MarkdownQuestionExplanationParser().parse(new StringReader(markdown)).get(9);

        assertEquals("中文解析。", explanation.chineseText());
        assertEquals("English explanation.", explanation.englishText());
    }

    @Test
    void rejectsMissingLanguage() {
        String markdown = """
                ## Question 9
                ### 中文
                中文解析。
                ---
                """;

        assertThrows(IllegalArgumentException.class,
                () -> new MarkdownQuestionExplanationParser().parse(new StringReader(markdown)));
    }
}
