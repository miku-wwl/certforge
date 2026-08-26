package com.certforge.controller;

import com.certforge.service.ProgressService;
import com.certforge.service.QuestionBankService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final QuestionBankService questionBankService;
    private final ProgressService progressService;

    public DashboardController(QuestionBankService questionBankService, ProgressService progressService) {
        this.questionBankService = questionBankService;
        this.progressService = progressService;
    }

    @GetMapping("/")
    public String dashboard(Model model, HttpSession session) {
        model.addAttribute("metadata", questionBankService.metadata());
        model.addAttribute("progress", progressService.summary(questionBankService.all().size()));
        model.addAttribute("hasContinue", session.getAttribute("certforge.review") != null);
        return "dashboard";
    }
}
