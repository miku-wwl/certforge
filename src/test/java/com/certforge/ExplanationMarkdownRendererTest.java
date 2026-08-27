package com.certforge;

import com.certforge.service.ExplanationMarkdownRenderer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExplanationMarkdownRendererTest {
    private final ExplanationMarkdownRenderer renderer = new ExplanationMarkdownRenderer();

    @Test
    void rendersStructuredExplanationAndEscapesRawHtml() {
        String markdown = """
                #### 考点背景
                **SageMaker Clarify** 检测 `<script>alert(1)</script>`。
                #### AWS 服务角色
                | AWS 服务 | 角色比喻 | 在本题中的作用 |
                | --- | --- | --- |
                | `CloudWatch` | 警报器 | 监控指标 |
                #### 逐项排除
                - **A：** 自定义开发过多。
                """;

        String html = renderer.render(markdown);

        assertTrue(html.contains("<h3>考点背景</h3>"));
        assertTrue(html.contains("<strong>SageMaker Clarify</strong>"));
        assertTrue(html.contains("<table class=\"explanation-table\">"));
        assertTrue(html.contains("<code>CloudWatch</code>"));
        assertTrue(html.contains("<ul><li>"));
        assertTrue(html.contains("&lt;script&gt;alert(1)&lt;/script&gt;"));
        assertFalse(html.contains("<script>"));
    }
}
