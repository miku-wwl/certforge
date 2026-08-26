package com.certforge.controller;

import com.certforge.dto.QuestionViewDto;
import com.certforge.dto.WrongQuestionRowDto;
import com.certforge.service.ProgressService;
import com.certforge.service.QuestionBankService;
import com.certforge.service.QuestionViewMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WrongController {
    private final ProgressService progressService;
    private final QuestionBankService questionBankService;
    private final QuestionViewMapper questionViewMapper;

    public WrongController(ProgressService progressService, QuestionBankService questionBankService,
                           QuestionViewMapper questionViewMapper) {
        this.progressService = progressService;
        this.questionBankService = questionBankService;
        this.questionViewMapper = questionViewMapper;
    }

    @GetMapping("/wrong")
    public String wrong(Model model) {
        List<WrongQuestionRowDto> rows = progressService.wrongQuestions().stream()
                .flatMap(summary -> questionBankService.findById(summary.questionId()).stream()
                        .map(question -> new WrongQuestionRowDto(
                                questionViewMapper.view(question, java.util.Set.of(), false), summary.incorrectCount(),
                                summary.lastSelectedAnswers(), summary.lastAttempt(), summary.mastered())))
                .toList();
        model.addAttribute("wrongQuestions", rows);
        return "wrong";
    }
}
