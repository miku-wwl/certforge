package com.certforge.controller;

import com.certforge.service.ProgressService;
import com.certforge.i18n.LanguageContext;
import com.certforge.service.AnswerExportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/data")
public class DataManagementController {
    private static final String REVIEW_SESSION_KEY = "certforge.review";
    private static final String EXAM_SESSION_KEY = "certforge.exam";

    private final ProgressService progressService;
    private final AnswerExportService answerExportService;
    private final LanguageContext languageContext;

    public DataManagementController(ProgressService progressService, AnswerExportService answerExportService,
                                    LanguageContext languageContext) {
        this.progressService = progressService;
        this.answerExportService = answerExportService;
        this.languageContext = languageContext;
    }

    @GetMapping(value = "/export.csv", produces = "text/csv")
    public ResponseEntity<byte[]> export() {
        byte[] csv = answerExportService.export(languageContext.current());
        return ResponseEntity.ok()
                .contentType(new MediaType("text", "csv", java.nio.charset.StandardCharsets.UTF_8))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"certforge-aip-c01-answer-status.csv\"")
                .body(csv);
    }

    @PostMapping("/reset")
    public String reset(HttpSession session) {
        progressService.clearAllRecords();
        session.removeAttribute(REVIEW_SESSION_KEY);
        session.removeAttribute(EXAM_SESSION_KEY);
        return "redirect:/?reset=success";
    }
}
