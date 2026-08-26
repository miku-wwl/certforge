package com.certforge;

import com.certforge.repository.AttemptRepository;
import com.certforge.repository.QuestionProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:certforge-test;DB_CLOSE_DELAY=-1")
class CertForgeIntegrationTest {
    @Autowired WebApplicationContext applicationContext;
    @Autowired QuestionProgressRepository progressRepository;
    @Autowired AttemptRepository attemptRepository;
    private MockMvc mockMvc;

    @BeforeEach
    void cleanDatabase() {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
        attemptRepository.deleteAll();
        progressRepository.deleteAll();
    }

    @Test
    void rendersDashboardStudyAndReviewWithoutLeakingAnswerBeforeCheck() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
        mockMvc.perform(get("/study")).andExpect(status().isOk());

        MvcResult start = mockMvc.perform(post("/review/start").param("selection", "range").param("range", "1-1"))
                .andExpect(redirectedUrl("/review")).andReturn();
        MockHttpSession session = (MockHttpSession) start.getRequest().getSession(false);
        String before = mockMvc.perform(get("/review").session(session)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertFalse(before.contains("Correct Answer"));
        assertFalse(before.contains("正确答案"));
        assertFalse(before.contains("58%"));

        String after = mockMvc.perform(post("/review/check").session(session).param("answers", "C"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(after.contains("正确答案"));
        assertTrue(after.contains("58%"));

        mockMvc.perform(post("/study/star").param("questionId", "aip-c01-q-1"))
                .andExpect(status().is3xxRedirection());
        assertTrue(progressRepository.findById("aip-c01-q-1").orElseThrow().isStarred());
        mockMvc.perform(get("/wrong")).andExpect(status().isOk());
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
    }

    @Test
    void rendersEnglishQuestionContentWhenEnglishIsSelected() throws Exception {
        String english = mockMvc.perform(get("/study").param("lang", "en"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(english.contains("A retail company has a generative AI"));
        assertTrue(english.contains("Toggle light/dark mode"));
        assertFalse(english.contains("一家零售公司拥有"));
    }
}
