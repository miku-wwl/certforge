package com.certforge.controller;

import com.certforge.domain.PracticeSession;
import com.certforge.domain.Question;
import com.certforge.domain.persistence.QuestionProgressEntity;
import com.certforge.dto.CheckResultDto;
import com.certforge.dto.NavigatorItemDto;
import com.certforge.dto.QuestionViewDto;
import com.certforge.service.PracticeService;
import com.certforge.service.ProgressService;
import com.certforge.service.QuestionBankService;
import com.certforge.service.QuestionViewMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class ReviewController {
    private static final String SESSION_KEY = "certforge.review";

    private final QuestionBankService questionBankService;
    private final PracticeService practiceService;
    private final ProgressService progressService;
    private final QuestionViewMapper questionViewMapper;
    private final MessageSource messageSource;

    public ReviewController(QuestionBankService questionBankService, PracticeService practiceService,
                            ProgressService progressService, QuestionViewMapper questionViewMapper,
                            MessageSource messageSource) {
        this.questionBankService = questionBankService;
        this.practiceService = practiceService;
        this.progressService = progressService;
        this.questionViewMapper = questionViewMapper;
        this.messageSource = messageSource;
    }

    @GetMapping("/review")
    public String review(@RequestParam(required = false) String filter,
                         @RequestParam(defaultValue = "false") boolean reset,
                         HttpSession session, Model model) {
        if (reset) {
            session.removeAttribute(SESSION_KEY);
            setupModel(model, filter);
            return "review-setup";
        }
        ReviewState state = state(session);
        if (state == null || state.practiceSession().size() == 0) {
            state = startDefaultSession(session);
        }
        render(model, state);
        return "review";
    }

    @PostMapping("/review/start")
    public String start(@RequestParam(defaultValue = "all") String selection,
                        @RequestParam(required = false) String range,
                        @RequestParam(required = false) Integer randomCount,
                        @RequestParam(defaultValue = "ALL") String type,
                        @RequestParam(required = false) String topic,
                        @RequestParam(required = false) String search,
                        @RequestParam(defaultValue = "false") boolean starredOnly,
                        @RequestParam(defaultValue = "false") boolean wrongOnly,
                        @RequestParam(defaultValue = "false") boolean unansweredOnly,
                        @RequestParam(defaultValue = "false") boolean shuffleQuestions,
                        @RequestParam(defaultValue = "false") boolean shuffleOptions,
                        HttpSession session, Model model) {
        PracticeService.SelectionCriteria criteria = criteria(selection, range, randomCount, type, topic, search,
                starredOnly, wrongOnly, unansweredOnly, shuffleQuestions);
        List<Question> questions = practiceService.select(criteria);
        if (questions.isEmpty()) {
            setupModel(model, selection);
            model.addAttribute("error", messageSource.getMessage("review.error", null,
                    LocaleContextHolder.getLocale()));
            return "review-setup";
        }
        PracticeSession practiceSession = new PracticeSession(questions.stream().map(Question::id).toList(), shuffleOptions);
        prepareOptionOrders(practiceSession, questions, shuffleOptions);
        session.setAttribute(SESSION_KEY, new ReviewState(practiceSession));
        return "redirect:/review";
    }

    @PostMapping("/review/check")
    public String check(@RequestParam(name = "answers", required = false) String[] answers,
                        HttpSession session) {
        ReviewState state = requiredState(session);
        Question question = questionBankService.require(state.practiceSession().currentQuestionId());
        String questionId = question.id();
        if (!state.checkedResults().containsKey(questionId)) {
            Set<String> selected = practiceService.normalize(answers == null ? List.of() : List.of(answers));
            state.practiceSession().answer(questionId, selected);
            boolean correct = practiceService.grade(question, selected);
            progressService.recordAttempt(practiceService.attempt(question, selected, com.certforge.domain.PracticeMode.REVIEW));
            state.checkedResults().put(questionId, correct);
        }
        return "redirect:/review";
    }

    @PostMapping("/review/next")
    public String next(HttpSession session) {
        requiredState(session).practiceSession().next();
        return "redirect:/review";
    }

    @PostMapping("/review/previous")
    public String previous(HttpSession session) {
        requiredState(session).practiceSession().previous();
        return "redirect:/review";
    }

    @PostMapping("/review/goto")
    public String goTo(@RequestParam int index, HttpSession session) {
        requiredState(session).practiceSession().moveTo(index);
        return "redirect:/review";
    }

    @PostMapping("/review/retry")
    public String retry(HttpSession session) {
        ReviewState state = requiredState(session);
        String questionId = state.practiceSession().currentQuestionId();
        state.practiceSession().answer(questionId, Set.of());
        state.checkedResults().remove(questionId);
        return "redirect:/review";
    }

    @PostMapping("/review/star")
    public String star(HttpSession session) {
        progressService.toggleStar(requiredState(session).practiceSession().currentQuestionId());
        return "redirect:/review";
    }

    private void render(Model model, ReviewState state) {
        Question question = questionBankService.require(state.practiceSession().currentQuestionId());
        String questionId = question.id();
        Set<String> selected = state.practiceSession().answerFor(questionId);
        boolean checked = state.checkedResults().containsKey(questionId);
        QuestionViewDto questionView = questionViewMapper.review(question, selected, checked,
                state.practiceSession().optionOrderFor(question.id()));
        CheckResultDto result = checked ? questionViewMapper.result(question, selected, state.checkedResults().get(questionId)) : null;

        model.addAttribute("question", questionView);
        model.addAttribute("checkResult", result);
        model.addAttribute("position", state.practiceSession().currentIndex() + 1);
        model.addAttribute("total", state.practiceSession().size());
        model.addAttribute("hasPrevious", state.practiceSession().currentIndex() > 0);
        model.addAttribute("hasNext", state.practiceSession().currentIndex() < state.practiceSession().size() - 1);
        model.addAttribute("checked", checked);
        model.addAttribute("navigator", navigator(state));
    }

    private List<NavigatorItemDto> navigator(ReviewState state) {
        Map<String, QuestionProgressEntity> progress = progressService.progressByQuestion();
        List<NavigatorItemDto> items = new ArrayList<>();
        for (int index = 0; index < state.practiceSession().size(); index++) {
            Question question = questionBankService.require(state.practiceSession().questionIds().get(index));
            QuestionProgressEntity saved = progress.get(question.id());
            String stateName = state.checkedResults().containsKey(question.id())
                    ? (state.checkedResults().get(question.id()) ? "correct" : "incorrect")
                    : saved != null && saved.isHasAnswered()
                    ? (saved.isLastCorrect() ? "correct" : "incorrect") : "unanswered";
            items.add(new NavigatorItemDto(question.questionNumber(), question.id(), stateName,
                    index == state.practiceSession().currentIndex(), saved != null && saved.isStarred()));
        }
        return items;
    }

    private void setupModel(Model model, String filter) {
        model.addAttribute("topics", questionBankService.topics());
        model.addAttribute("selectedFilter", filter == null || filter.isBlank() ? "all" : filter);
        model.addAttribute("totalQuestions", questionBankService.all().size());
    }

    private void prepareOptionOrders(PracticeSession session, List<Question> questions, boolean shuffleOptions) {
        for (Question question : questions) {
            List<String> labels = question.options().stream().map(option -> option.label())
                    .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
            if (shuffleOptions) java.util.Collections.shuffle(labels);
            session.setOptionOrder(question.id(), labels);
        }
    }

    private ReviewState startDefaultSession(HttpSession session) {
        List<Question> questions = questionBankService.all();
        PracticeSession practiceSession = new PracticeSession(questions.stream().map(Question::id).toList());
        prepareOptionOrders(practiceSession, questions, false);
        ReviewState state = new ReviewState(practiceSession);
        session.setAttribute(SESSION_KEY, state);
        return state;
    }

    private PracticeService.SelectionCriteria criteria(String selection, String range, Integer randomCount, String type,
                                                       String topic, String search, boolean starredOnly, boolean wrongOnly,
                                                       boolean unansweredOnly, boolean shuffleQuestions) {
        String normalizedSelection = selection == null ? "all" : selection.toLowerCase();
        Set<Integer> numbers = "range".equals(normalizedSelection) ? PracticeService.parseRange(range) : null;
        Integer random = "random".equals(normalizedSelection) ? Math.max(1, Math.min(questionBankService.all().size(),
                randomCount == null ? 10 : randomCount)) : null;
        return new PracticeService.SelectionCriteria(numbers, search, topic, parseType(type),
                starredOnly || "starred".equals(normalizedSelection),
                wrongOnly || "wrong".equals(normalizedSelection),
                unansweredOnly || "unanswered".equals(normalizedSelection), random, shuffleQuestions);
    }

    private PracticeService.SelectionType parseType(String type) {
        try {
            return PracticeService.SelectionType.valueOf(type == null ? "ALL" : type.toUpperCase());
        } catch (IllegalArgumentException exception) {
            return PracticeService.SelectionType.ALL;
        }
    }

    private ReviewState state(HttpSession session) {
        Object value = session.getAttribute(SESSION_KEY);
        return value instanceof ReviewState reviewState ? reviewState : null;
    }

    private ReviewState requiredState(HttpSession session) {
        ReviewState state = state(session);
        if (state == null || state.practiceSession().size() == 0) {
            throw new IllegalStateException("No active review session");
        }
        return state;
    }

    private static final class ReviewState {
        private final PracticeSession practiceSession;
        private final Map<String, Boolean> checkedResults = new HashMap<>();

        private ReviewState(PracticeSession practiceSession) {
            this.practiceSession = practiceSession;
        }

        private PracticeSession practiceSession() { return practiceSession; }
        private Map<String, Boolean> checkedResults() { return checkedResults; }
    }
}
