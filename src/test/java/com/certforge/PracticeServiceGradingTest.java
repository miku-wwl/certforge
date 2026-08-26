package com.certforge;

import com.certforge.domain.Question;
import com.certforge.domain.QuestionOption;
import com.certforge.service.PracticeService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PracticeServiceGradingTest {
    private final PracticeService service = new PracticeService(null, null);

    @Test
    void gradesSingleChoiceExactly() {
        Question question = question(Set.of("C"));
        assertTrue(service.grade(question, Set.of("C")));
        assertFalse(service.grade(question, Set.of("A")));
        assertFalse(service.grade(question, Set.of("C", "D")));
    }

    @Test
    void gradesMultipleChoiceByExactSetNotContains() {
        Question question = question(Set.of("A", "D", "F"));
        assertTrue(service.grade(question, Set.of("A", "D", "F")));
        assertFalse(service.grade(question, Set.of("A", "D")));
        assertFalse(service.grade(question, Set.of("A", "F")));
        assertFalse(service.grade(question, Set.of("A", "D", "F", "G")));
    }

    private Question question(Set<String> answers) {
        return new Question("test", 1, "Topic", "text", List.of(
                new QuestionOption("A", "A", false), new QuestionOption("B", "B", false),
                new QuestionOption("C", "C", false), new QuestionOption("D", "D", false),
                new QuestionOption("E", "E", false), new QuestionOption("F", "F", false)), answers, List.of());
    }
}
