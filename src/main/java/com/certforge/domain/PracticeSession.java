package com.certforge.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class PracticeSession {
    private final List<String> questionIds;
    private final Map<String, Set<String>> answers = new LinkedHashMap<>();
    private final Map<String, List<String>> optionOrders = new LinkedHashMap<>();
    private final Set<String> flagged = new LinkedHashSet<>();
    private int currentIndex;
    private final long startedAtMillis;
    private final boolean shuffleOptions;

    public PracticeSession(Collection<String> questionIds) {
        this(questionIds, false);
    }

    public PracticeSession(Collection<String> questionIds, boolean shuffleOptions) {
        this.questionIds = new ArrayList<>(questionIds);
        this.startedAtMillis = System.currentTimeMillis();
        this.shuffleOptions = shuffleOptions;
    }

    public List<String> questionIds() {
        return Collections.unmodifiableList(questionIds);
    }

    public String currentQuestionId() {
        return questionIds.isEmpty() ? null : questionIds.get(currentIndex);
    }

    public int currentIndex() {
        return currentIndex;
    }

    public int size() {
        return questionIds.size();
    }

    public void moveTo(int index) {
        if (!questionIds.isEmpty()) {
            currentIndex = Math.max(0, Math.min(index, questionIds.size() - 1));
        }
    }

    public void next() {
        moveTo(currentIndex + 1);
    }

    public void previous() {
        moveTo(currentIndex - 1);
    }

    public void answer(String questionId, Set<String> selectedAnswers) {
        answers.put(questionId, Set.copyOf(selectedAnswers));
    }

    public Set<String> answerFor(String questionId) {
        return answers.getOrDefault(questionId, Set.of());
    }

    public Map<String, Set<String>> answers() {
        return Collections.unmodifiableMap(answers);
    }

    public void toggleFlag(String questionId) {
        if (!flagged.add(questionId)) {
            flagged.remove(questionId);
        }
    }

    public boolean isFlagged(String questionId) {
        return flagged.contains(questionId);
    }

    public Set<String> flagged() {
        return Collections.unmodifiableSet(flagged);
    }

    public long startedAtMillis() {
        return startedAtMillis;
    }

    public boolean shuffleOptions() {
        return shuffleOptions;
    }

    public void setOptionOrder(String questionId, Collection<String> labels) {
        optionOrders.put(questionId, List.copyOf(labels));
    }

    public List<String> optionOrderFor(String questionId) {
        return optionOrders.getOrDefault(questionId, List.of());
    }
}
