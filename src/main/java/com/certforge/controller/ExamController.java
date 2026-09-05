package com.certforge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * The former AWS AIP exam mode is retired while the active bank is the SRE
 * foundations short-answer curriculum. Keep old endpoints as safe redirects
 * for bookmarks and old clients.
 */
@Controller
public class ExamController {
    @GetMapping({"/exam", "/exam/session"})
    public String retiredExam() {
        return "redirect:/study";
    }

    @PostMapping({"/exam/start", "/exam/save", "/exam/goto", "/exam/flag", "/exam/submit"})
    public String retiredExamPost() {
        return "redirect:/study";
    }
}
