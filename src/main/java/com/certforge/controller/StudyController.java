package com.certforge.controller;

import com.certforge.domain.Question;
import com.certforge.dto.StudyQuestionDto;
import com.certforge.service.PracticeService;
import com.certforge.service.ProgressService;
import com.certforge.service.QuestionBankService;
import com.certforge.service.QuestionViewMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@Controller
public class StudyController {
    private final QuestionBankService questionBankService;
    private final PracticeService practiceService;
    private final ProgressService progressService;
    private final QuestionViewMapper questionViewMapper;

    public StudyController(QuestionBankService questionBankService, PracticeService practiceService,
                           ProgressService progressService, QuestionViewMapper questionViewMapper) {
        this.questionBankService = questionBankService;
        this.practiceService = practiceService;
        this.progressService = progressService;
        this.questionViewMapper = questionViewMapper;
    }

    @GetMapping("/study")
    public String study(@RequestParam(required = false) Integer q,
                        @RequestParam(required = false) String search,
                        @RequestParam(required = false) String topic,
                        @RequestParam(defaultValue = "ALL") String type,
                        @RequestParam(defaultValue = "false") boolean starredOnly,
                        @RequestParam(defaultValue = "false") boolean wrongOnly,
                        Model model) {
        PracticeService.SelectionCriteria criteria = new PracticeService.SelectionCriteria(
                null, search, topic, parseType(type), starredOnly, wrongOnly, false, null, false);
        List<Question> questions = practiceService.select(criteria);
        Question selected = q == null ? (questions.isEmpty() ? null : questions.get(0))
                : questions.stream().filter(question -> question.questionNumber() == q).findFirst()
                .orElse(questions.isEmpty() ? null : questions.get(0));
        StudyQuestionDto studyQuestion = selected == null ? null : questionViewMapper.study(selected);
        model.addAttribute("studyQuestion", studyQuestion);
        model.addAttribute("hasQuestion", selected != null);
        model.addAttribute("filterCount", questions.size());
        model.addAttribute("topics", questionBankService.topics());
        model.addAttribute("search", search == null ? "" : search);
        model.addAttribute("selectedTopic", topic == null ? "" : topic);
        model.addAttribute("selectedType", parseType(type).name());
        model.addAttribute("starredOnly", starredOnly);
        model.addAttribute("wrongOnly", wrongOnly);
        if (selected != null) {
            int index = questions.indexOf(selected);
            model.addAttribute("previousUrl", index > 0 ? navigationUrl(questions.get(index - 1).questionNumber(), search, topic, type,
                    starredOnly, wrongOnly) : null);
            model.addAttribute("nextUrl", index < questions.size() - 1 ? navigationUrl(questions.get(index + 1).questionNumber(), search, topic, type,
                    starredOnly, wrongOnly) : null);
        }
        return "study";
    }

    @org.springframework.web.bind.annotation.PostMapping("/study/star")
    public String star(@RequestParam String questionId) {
        progressService.toggleStar(questionId);
        return "redirect:/study?q=" + questionBankService.require(questionId).questionNumber();
    }

    @GetMapping("/starred")
    public String starred() {
        return "redirect:/study?starredOnly=true";
    }

    private PracticeService.SelectionType parseType(String type) {
        try {
            return PracticeService.SelectionType.valueOf(type == null ? "ALL" : type.toUpperCase());
        } catch (IllegalArgumentException exception) {
            return PracticeService.SelectionType.ALL;
        }
    }

    private String navigationUrl(int questionNumber, String search, String topic, String type,
                                 boolean starredOnly, boolean wrongOnly) {
        StringBuilder url = new StringBuilder("/study?q=").append(questionNumber);
        append(url, "search", search);
        append(url, "topic", topic);
        if (type != null && !type.equalsIgnoreCase("ALL")) append(url, "type", type);
        if (starredOnly) append(url, "starredOnly", "true");
        if (wrongOnly) append(url, "wrongOnly", "true");
        return url.toString();
    }

    private void append(StringBuilder url, String name, String value) {
        if (value != null && !value.isBlank()) {
            url.append('&').append(name).append('=').append(URLEncoder.encode(value, StandardCharsets.UTF_8));
        }
    }
}
