package com.certforge.controller;

import com.certforge.service.ProgressService;
import com.certforge.i18n.LanguageContext;
import com.certforge.service.AnswerExportService;
import com.certforge.service.AnswerImportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/data")
public class DataManagementController {
    private static final String REVIEW_SESSION_KEY = "certforge.review";
    private static final String EXAM_SESSION_KEY = "certforge.exam";

    private final ProgressService progressService;
    private final AnswerExportService answerExportService;
    private final AnswerImportService answerImportService;
    private final LanguageContext languageContext;

    public DataManagementController(ProgressService progressService, AnswerExportService answerExportService,
                                    AnswerImportService answerImportService, LanguageContext languageContext) {
        this.progressService = progressService;
        this.answerExportService = answerExportService;
        this.answerImportService = answerImportService;
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

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String importData(@RequestParam("file") MultipartFile file, HttpSession session,
                              RedirectAttributes redirectAttributes) {
        try {
            AnswerImportService.ImportResult result = answerImportService.importCsv(file.getBytes());
            session.removeAttribute(REVIEW_SESSION_KEY);
            session.removeAttribute(EXAM_SESSION_KEY);
            redirectAttributes.addFlashAttribute("importComplete", true);
            redirectAttributes.addFlashAttribute("importQuestionCount", result.questionCount());
            redirectAttributes.addFlashAttribute("importAttemptCount", result.attemptCount());
        } catch (AnswerImportService.ImportException exception) {
            redirectAttributes.addFlashAttribute("importError",
                    "CSV 导入失败 / CSV import failed: " + exception.getMessage());
        } catch (java.io.IOException exception) {
            redirectAttributes.addFlashAttribute("importError",
                    "CSV 文件读取失败 / could not read CSV file");
        }
        return "redirect:/";
    }

    @PostMapping("/reset")
    public String reset(HttpSession session) {
        progressService.clearAllRecords();
        session.removeAttribute(REVIEW_SESSION_KEY);
        session.removeAttribute(EXAM_SESSION_KEY);
        return "redirect:/?reset=success";
    }
}
