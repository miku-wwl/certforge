package com.certforge;

import com.certforge.domain.PracticeMode;
import com.certforge.domain.QuestionAttempt;
import com.certforge.repository.AttemptRepository;
import com.certforge.repository.QuestionProgressRepository;
import com.certforge.service.ProgressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:persistence-test;DB_CLOSE_DELAY=-1")
class ProgressPersistenceTest {
    @Autowired QuestionProgressRepository progressRepository;
    @Autowired AttemptRepository attemptRepository;
    @Autowired ProgressService progressService;

    @BeforeEach
    void setUp() {
        attemptRepository.deleteAll();
        progressRepository.deleteAll();
    }

    @Test
    void starAttemptWrongHistoryAndMasteryPersistInRepositories() {
        assertTrue(progressService.toggleStar("q-1"));
        progressService.recordAttempt(new QuestionAttempt("q-1", Set.of("A"), false, true, Instant.now(), PracticeMode.REVIEW));
        progressService.recordAttempt(new QuestionAttempt("q-1", Set.of("C"), true, true, Instant.now(), PracticeMode.REVIEW));
        progressService.recordAttempt(new QuestionAttempt("q-1", Set.of("C"), true, true, Instant.now(), PracticeMode.REVIEW));

        var saved = progressRepository.findById("q-1").orElseThrow();
        assertTrue(saved.isStarred());
        assertEquals(3, saved.getAttemptCount());
        assertEquals(1, saved.getIncorrectCount());
        assertEquals(2, saved.getConsecutiveCorrect());
        assertEquals(3, attemptRepository.findByQuestionIdOrderByCheckedAtDesc("q-1").size());
        assertEquals(1, progressService.wrongQuestions().size());
        assertTrue(progressService.wrongQuestions().get(0).mastered());
    }
}
