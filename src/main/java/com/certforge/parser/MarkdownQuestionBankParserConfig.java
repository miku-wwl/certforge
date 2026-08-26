package com.certforge.parser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MarkdownQuestionBankParserConfig {
    @Bean
    public MarkdownQuestionBankParser markdownQuestionBankParser() {
        return new MarkdownQuestionBankParser();
    }
}
