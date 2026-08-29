package com.certforge.controller;

import com.certforge.service.ProgressService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/data")
public class DataManagementController {
    private static final String REVIEW_SESSION_KEY = "certforge.review";
    private static final String EXAM_SESSION_KEY = "certforge.exam";

    private final ProgressService progressService;

    public DataManagementController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @PostMapping("/reset")
    public String reset(HttpSession session) {
        progressService.clearAllRecords();
        session.removeAttribute(REVIEW_SESSION_KEY);
        session.removeAttribute(EXAM_SESSION_KEY);
        return "redirect:/?reset=success";
    }
}
