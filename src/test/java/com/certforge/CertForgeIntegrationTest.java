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

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void rendersSreDashboardStudyAndShortAnswerReview() throws Exception {
        String dashboard = mockMvc.perform(get("/")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertTrue(dashboard.contains("SRE-FOUNDATIONS"));
        assertTrue(dashboard.contains("560"));
        assertFalse(dashboard.contains("/exam"));

        String study = mockMvc.perform(get("/study")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertTrue(study.contains("参考答案"));
        assertTrue(study.contains("Git 内部原理"));
        assertTrue(study.contains("基础定义"));

        MvcResult start = mockMvc.perform(post("/review/start").param("selection", "range").param("range", "1-2"))
                .andExpect(redirectedUrl("/review")).andReturn();
        MockHttpSession session = (MockHttpSession) start.getRequest().getSession(false);
        String before = mockMvc.perform(get("/review").session(session)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertTrue(before.contains("先自己回答，再查看参考答案"));
        assertTrue(before.contains("data-copy-question-answer"));
        assertFalse(before.contains("data-answer-reveal"));

        mockMvc.perform(post("/review/check").session(session)).andExpect(redirectedUrl("/review"));
        String after = mockMvc.perform(get("/review").session(session)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertTrue(after.contains("参考答案"));
        assertTrue(after.contains("SRE 基础知识解析"));
        assertTrue(after.contains("data-answer-reveal"));
        assertEquals(0, attemptRepository.count());

        mockMvc.perform(post("/review/next").session(session)).andExpect(redirectedUrl("/review"));
        assertTrue(mockMvc.perform(get("/review").session(session)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().contains("第 2 题"));
    }

    @Test
    void opensInstantPracticeDirectlyAndSupportsSetup() throws Exception {
        String direct = mockMvc.perform(get("/review")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertTrue(direct.contains("第 1 题"));
        String setup = mockMvc.perform(get("/review").param("reset", "true"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(setup.contains("设置一轮练习"));
        assertTrue(setup.contains("简答题"));
    }

    @Test
    void retiredExamEndpointsRedirectToStudy() throws Exception {
        mockMvc.perform(get("/exam")).andExpect(redirectedUrl("/study"));
        mockMvc.perform(get("/exam/session")).andExpect(redirectedUrl("/study"));
        mockMvc.perform(post("/exam/start")).andExpect(redirectedUrl("/study"));
    }

    @Test
    void clearsLocalRecordsAndActiveReviewSession() throws Exception {
        mockMvc.perform(post("/study/star").param("questionId", "sre-foundations-q-1"))
                .andExpect(status().is3xxRedirection());
        MvcResult reviewStart = mockMvc.perform(post("/review/start")
                        .param("selection", "range").param("range", "1-1"))
                .andExpect(redirectedUrl("/review")).andReturn();
        MockHttpSession session = (MockHttpSession) reviewStart.getRequest().getSession(false);
        mockMvc.perform(post("/review/check").session(session)).andExpect(redirectedUrl("/review"));

        assertEquals(0, attemptRepository.count());
        assertTrue(progressRepository.findById("sre-foundations-q-1").orElseThrow().isStarred());
        assertTrue(session.getAttribute("certforge.review") != null);

        mockMvc.perform(post("/data/reset").session(session)).andExpect(redirectedUrl("/?reset=success"));
        assertEquals(0, attemptRepository.count());
        assertEquals(0, progressRepository.count());
        assertNull(session.getAttribute("certforge.review"));
    }

    @Test
    void exportsAndImportsSreAnswerStatusCsv() throws Exception {
        mockMvc.perform(post("/study/star").param("questionId", "sre-foundations-q-1"))
                .andExpect(status().is3xxRedirection());
        MvcResult export = mockMvc.perform(get("/data/export.csv")).andExpect(status().isOk()).andReturn();
        String csv = new String(export.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);
        assertTrue(csv.startsWith("\uFEFF题号,题目ID,主题,题型"));
        assertTrue(csv.contains("sre-foundations-q-1"));
        assertTrue(csv.contains("简答题"));
        assertTrue(csv.contains("`.git`"));

        progressRepository.deleteAll();
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart("/data/import")
                        .file(new MockMultipartFile("file", "backup.csv", "text/csv", export.getResponse().getContentAsByteArray())))
                .andExpect(redirectedUrl("/"));
        assertTrue(progressRepository.findById("sre-foundations-q-1").orElseThrow().isStarred());
    }

    @Test
    void rendersEnglishUiWithChineseSreContentFallback() throws Exception {
        String english = mockMvc.perform(get("/study").param("lang", "en"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(english.contains("Toggle light/dark mode"));
        assertTrue(english.contains("Reference answer"));
        assertTrue(english.contains("Git 内部原理"));
    }

    @Test
    void loadsAllFiveHundredSixtySreReferenceAnswers() {
        assertEquals(560, explanationService.count());
    }
}
