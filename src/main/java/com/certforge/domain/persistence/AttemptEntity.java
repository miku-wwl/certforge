package com.certforge.domain.persistence;

import com.certforge.domain.PracticeMode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "question_attempt")
public class AttemptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String questionId;
    private String selectedAnswers;
    private boolean correct;
    private boolean answered;
    private Instant checkedAt;
    private PracticeMode practiceMode;

    protected AttemptEntity() {
    }

    public AttemptEntity(String questionId, String selectedAnswers, boolean correct, boolean answered,
                         Instant checkedAt, PracticeMode practiceMode) {
        this.questionId = questionId;
        this.selectedAnswers = selectedAnswers;
        this.correct = correct;
        this.answered = answered;
        this.checkedAt = checkedAt;
        this.practiceMode = practiceMode;
    }

    public Long getId() { return id; }
    public String getQuestionId() { return questionId; }
    public String getSelectedAnswers() { return selectedAnswers; }
    public boolean isCorrect() { return correct; }
    public boolean isAnswered() { return answered; }
    public Instant getCheckedAt() { return checkedAt; }
    public PracticeMode getPracticeMode() { return practiceMode; }
}
