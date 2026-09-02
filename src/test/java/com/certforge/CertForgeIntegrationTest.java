package com.certforge;

import com.certforge.repository.AttemptRepository;
import com.certforge.repository.QuestionProgressRepository;
import com.certforge.service.QuestionExplanationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:certforge-test;DB_CLOSE_DELAY=-1")
class CertForgeIntegrationTest {
    @Autowired WebApplicationContext applicationContext;
    @Autowired QuestionProgressRepository progressRepository;
    @Autowired AttemptRepository attemptRepository;
    @Autowired QuestionExplanationService explanationService;
    private MockMvc mockMvc;

    @BeforeEach
    void cleanDatabase() {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
        attemptRepository.deleteAll();
        progressRepository.deleteAll();
    }

    @Test
    void rendersDashboardStudyAndReviewWithCopyAvailableBeforeCheck() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
        mockMvc.perform(get("/study")).andExpect(status().isOk());

        MvcResult start = mockMvc.perform(post("/review/start").param("selection", "range").param("range", "1-2"))
                .andExpect(redirectedUrl("/review")).andReturn();
        MockHttpSession session = (MockHttpSession) start.getRequest().getSession(false);
        String before = mockMvc.perform(get("/review").session(session)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertFalse(before.contains("58%"));
        assertFalse(before.contains("data-answer-reveal"));
        assertTrue(before.contains("data-copy-question"));
        assertTrue(before.contains("复制题目"));
        assertTrue(before.contains("data-copy-question-answer"));
        assertTrue(before.contains("复制题目+答案+题解"));
        assertTrue(before.contains("data-copy-answer-source"));
        assertTrue(before.contains("data-copy-answer"));
        assertTrue(before.contains("data-copy-explanation"));

        mockMvc.perform(post("/review/check").session(session).param("answers", "C"))
                .andExpect(redirectedUrl("/review"));
        String after = mockMvc.perform(get("/review").session(session))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(after.contains("正确答案"));
        assertTrue(after.contains("58%"));
        assertTrue(after.contains("考点背景"));
        assertTrue(after.contains("场景比喻"));
        assertTrue(after.contains("explanation-table"));
        assertTrue(after.contains("data-answer-reveal"));
        assertTrue(after.contains("data-copy-question-answer"));
        assertTrue(after.contains("复制题目+答案+题解"));
        assertTrue(after.contains("data-copy-answer"));
        assertTrue(after.contains("data-copy-explanation"));
        assertTrue(after.contains("下一题 →"));
        assertFalse(after.contains("data-explanation-modal"));
        assertFalse(after.contains("role=\"dialog\""));

        mockMvc.perform(post("/review/next").session(session))
                .andExpect(redirectedUrl("/review"));
        String nextQuestion = mockMvc.perform(get("/review").session(session))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(nextQuestion.contains("第 2 题"));

        mockMvc.perform(post("/study/star").param("questionId", "aip-c01-q-1"))
                .andExpect(status().is3xxRedirection());
        assertTrue(progressRepository.findById("aip-c01-q-1").orElseThrow().isStarred());
        mockMvc.perform(get("/wrong")).andExpect(status().isOk());
    }

    @Test
    void opensInstantPracticeDirectlyWithoutSetup() throws Exception {
        String direct = mockMvc.perform(get("/review"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(direct.contains("第 1 题"));
        assertTrue(direct.contains("data-copy-question"));
        assertFalse(direct.contains("设置一轮练习"));

        String setup = mockMvc.perform(get("/review").param("reset", "true"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(setup.contains("设置一轮练习"));
        assertFalse(setup.contains("第 1 题"));
    }

    @Test
    void wrongReviewRendersRecordedListWithoutStartingInstantPractice() throws Exception {
        MvcResult start = mockMvc.perform(post("/review/start")
                        .param("selection", "range").param("range", "1-1"))
                .andExpect(redirectedUrl("/review")).andReturn();
        MockHttpSession session = (MockHttpSession) start.getRequest().getSession(false);
        mockMvc.perform(post("/review/check").session(session).param("answers", "A"))
                .andExpect(redirectedUrl("/review"));

        String wrongPage = mockMvc.perform(get("/wrong"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(wrongPage.contains("曾经答错的题目"));
        assertTrue(wrongPage.contains("第 1 题"));
        assertFalse(wrongPage.contains("开始错题练习"));
        assertFalse(wrongPage.contains("/review?filter=wrong"));
    }

    @Test
    void keepsExamAnswersServerSideUntilSubmit() throws Exception {
        MvcResult start = mockMvc.perform(post("/exam/start").param("number", "2").param("shuffleQuestions", "false"))
                .andExpect(redirectedUrl("/exam/session")).andReturn();
        MockHttpSession session = (MockHttpSession) start.getRequest().getSession(false);
        String before = mockMvc.perform(get("/exam/session").session(session)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertFalse(before.contains("Correct Answer"));
        assertFalse(before.contains("58%"));

        String result = mockMvc.perform(post("/exam/submit").session(session))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(result.contains("本轮成绩"));
        assertTrue(result.contains("正确答案"));
        assertTrue(result.contains("AI 解析"));
    }

    @Test
    void clearsLocalRecordsAndActivePracticeSessions() throws Exception {
        mockMvc.perform(post("/study/star").param("questionId", "aip-c01-q-1"))
                .andExpect(status().is3xxRedirection());
        MvcResult reviewStart = mockMvc.perform(post("/review/start")
                        .param("selection", "range").param("range", "1-1"))
                .andExpect(redirectedUrl("/review")).andReturn();
        MockHttpSession session = (MockHttpSession) reviewStart.getRequest().getSession(false);
        mockMvc.perform(post("/review/check").session(session).param("answers", "C"))
                .andExpect(redirectedUrl("/review"));
        mockMvc.perform(post("/exam/start").session(session).param("number", "1"))
                .andExpect(redirectedUrl("/exam/session"));

        assertTrue(attemptRepository.count() > 0);
        assertTrue(progressRepository.count() > 0);
        assertTrue(session.getAttribute("certforge.review") != null);
        assertTrue(session.getAttribute("certforge.exam") != null);

        mockMvc.perform(post("/data/reset").session(session))
                .andExpect(redirectedUrl("/?reset=success"));

        assertEquals(0, attemptRepository.count());
        assertEquals(0, progressRepository.count());
        assertNull(session.getAttribute("certforge.review"));
        assertNull(session.getAttribute("certforge.exam"));
        String dashboard = mockMvc.perform(get("/").param("reset", "success"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(dashboard.contains("本地做题记录已清空"));
    }

    @Test
    void exportsAnswerStatusAsExcelFriendlyUtf8Csv() throws Exception {
        MvcResult reviewStart = mockMvc.perform(post("/review/start")
                        .param("selection", "range").param("range", "1-1"))
                .andExpect(redirectedUrl("/review")).andReturn();
        MockHttpSession session = (MockHttpSession) reviewStart.getRequest().getSession(false);
        mockMvc.perform(post("/review/check").session(session).param("answers", "C"))
                .andExpect(redirectedUrl("/review"));

        MvcResult export = mockMvc.perform(get("/data/export.csv"))
                .andExpect(status().isOk()).andReturn();
        String csv = new String(export.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);
        assertTrue(export.getResponse().getContentType().startsWith("text/csv"));
        assertTrue(export.getResponse().getHeader("Content-Disposition").contains("attachment"));
        assertTrue(csv.startsWith("\uFEFF题号,题目ID,主题,题型"));
        assertTrue(csv.contains("aip-c01-q-1"));
        assertTrue(csv.contains("即时答题"));
        assertTrue(csv.contains("一家零售公司拥有"));
    }

    @Test
    void importsExportedCsvAndReplacesCurrentAnswerStatus() throws Exception {
        mockMvc.perform(post("/study/star").param("questionId", "aip-c01-q-1"))
                .andExpect(status().is3xxRedirection());
        MvcResult reviewStart = mockMvc.perform(post("/review/start")
                        .param("selection", "range").param("range", "1-1"))
                .andExpect(redirectedUrl("/review")).andReturn();
        MockHttpSession session = (MockHttpSession) reviewStart.getRequest().getSession(false);
        mockMvc.perform(post("/review/check").session(session).param("answers", "C"))
                .andExpect(redirectedUrl("/review"));
        byte[] csv = mockMvc.perform(get("/data/export.csv"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray();

        progressRepository.deleteAll();
        attemptRepository.deleteAll();
        assertEquals(0, progressRepository.count());
        assertEquals(0, attemptRepository.count());

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart("/data/import")
                        .file(new MockMultipartFile("file", "backup.csv", "text/csv", csv)).session(session))
                .andExpect(redirectedUrl("/"));
        assertTrue(progressRepository.findById("aip-c01-q-1").orElseThrow().isStarred());
        assertTrue(progressRepository.findById("aip-c01-q-1").orElseThrow().isHasAnswered());
        assertEquals(1, attemptRepository.count());
        assertNull(session.getAttribute("certforge.review"));
    }

    @Test
    void rendersEnglishQuestionContentWhenEnglishIsSelected() throws Exception {
        String english = mockMvc.perform(get("/study").param("lang", "en"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(english.contains("A retail company has a generative AI"));
        assertTrue(english.contains("Toggle light/dark mode"));
        assertFalse(english.contains("一家零售公司拥有"));

        MvcResult start = mockMvc.perform(post("/review/start").param("selection", "range").param("range", "1-1"))
                .andExpect(redirectedUrl("/review")).andReturn();
        MockHttpSession session = (MockHttpSession) start.getRequest().getSession(false);
        String englishReview = mockMvc.perform(get("/review").param("lang", "en").session(session))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(englishReview.contains("Copy question"));
        assertTrue(englishReview.contains("A retail company has a generative AI"));
        assertFalse(englishReview.contains("一家零售公司拥有"));

        mockMvc.perform(post("/review/check").session(session).param("answers", "C"))
                .andExpect(redirectedUrl("/review"));
        String englishChecked = mockMvc.perform(get("/review").session(session))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(englishChecked.contains("Exam focus and background"));
        assertTrue(englishChecked.contains("AWS service roles"));
    }

    @Test
    void loadsBilingualAiExplanationsForEveryQuestion() {
        assertTrue(explanationService.count() == 127);
    }
}
