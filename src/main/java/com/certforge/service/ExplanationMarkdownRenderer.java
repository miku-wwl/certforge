package com.certforge.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Renders the small, controlled Markdown subset used by bundled explanations.
 * All source text is escaped before formatting, so th:utext never receives raw authored HTML.
 */
@Component
public class ExplanationMarkdownRenderer {
    private static final Pattern CODE = Pattern.compile("`([^`]+)`");
    private static final Pattern BOLD = Pattern.compile("\\*\\*(.+?)\\*\\*");
    private static final Pattern ORDERED_ITEM = Pattern.compile("^\\d+\\.\\s+(.+)$");

    public String render(String markdown) {
        List<String> lines = markdown == null ? List.of() : markdown.lines().toList();
        StringBuilder html = new StringBuilder("<div class=\"rich-explanation\">");
        int index = 0;
        while (index < lines.size()) {
            String line = lines.get(index).trim();
            if (line.isBlank()) {
                index++;
                continue;
            }
            if (line.startsWith("#### ")) {
                html.append("<h3>").append(inline(line.substring(5))).append("</h3>");
                index++;
                continue;
            }
            if (isTableRow(line)) {
                List<List<String>> rows = new ArrayList<>();
                while (index < lines.size() && isTableRow(lines.get(index).trim())) {
                    List<String> cells = tableCells(lines.get(index).trim());
                    if (!isDelimiterRow(cells)) {
                        rows.add(cells);
                    }
                    index++;
                }
                appendTable(html, rows);
                continue;
            }
            if (line.startsWith("- ")) {
                html.append("<ul>");
                while (index < lines.size() && lines.get(index).trim().startsWith("- ")) {
                    html.append("<li>").append(inline(lines.get(index).trim().substring(2))).append("</li>");
                    index++;
                }
                html.append("</ul>");
                continue;
            }
            Matcher ordered = ORDERED_ITEM.matcher(line);
            if (ordered.matches()) {
                html.append("<ol>");
                while (index < lines.size()) {
                    Matcher item = ORDERED_ITEM.matcher(lines.get(index).trim());
                    if (!item.matches()) {
                        break;
                    }
                    html.append("<li>").append(inline(item.group(1))).append("</li>");
                    index++;
                }
                html.append("</ol>");
                continue;
            }
            html.append("<p>").append(inline(line)).append("</p>");
            index++;
        }
        return html.append("</div>").toString();
    }

    private static void appendTable(StringBuilder html, List<List<String>> rows) {
        if (rows.isEmpty()) {
            return;
        }
        html.append("<div class=\"explanation-table-wrap\"><table class=\"explanation-table\"><thead><tr>");
        for (String cell : rows.getFirst()) {
            html.append("<th>").append(inline(cell)).append("</th>");
        }
        html.append("</tr></thead><tbody>");
        for (int rowIndex = 1; rowIndex < rows.size(); rowIndex++) {
            html.append("<tr>");
            for (String cell : rows.get(rowIndex)) {
                html.append("<td>").append(inline(cell)).append("</td>");
            }
            html.append("</tr>");
        }
        html.append("</tbody></table></div>");
    }

    private static boolean isTableRow(String line) {
        return line.startsWith("|") && line.endsWith("|") && line.length() > 2;
    }

    private static List<String> tableCells(String line) {
        String[] raw = line.substring(1, line.length() - 1).split("\\|", -1);
        return java.util.Arrays.stream(raw).map(String::trim).toList();
    }

    private static boolean isDelimiterRow(List<String> cells) {
        return !cells.isEmpty() && cells.stream().allMatch(cell -> cell.matches(":?-{3,}:?"));
    }

    private static String inline(String source) {
        String formatted = escape(source);
        formatted = replace(CODE, formatted, "code");
        return replace(BOLD, formatted, "strong");
    }

    private static String replace(Pattern pattern, String source, String tag) {
        Matcher matcher = pattern.matcher(source);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(result, Matcher.quoteReplacement("<" + tag + ">" + matcher.group(1) + "</" + tag + ">"));
        }
        return matcher.appendTail(result).toString();
    }

    private static String escape(String value) {
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
