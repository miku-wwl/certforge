package com.certforge.controller;

import com.certforge.domain.PracticeSession;
import com.certforge.domain.Question;
import com.certforge.dto.ExamAnswerReviewDto;
import com.certforge.dto.ExamResultPageDto;
import com.certforge.dto.NavigatorItemDto;
import com.certforge.dto.QuestionViewDto;
import com.certforge.service.ExamService;
import com.certforge.service.PracticeService;
import com.certforge.service.QuestionBankService;
import com.certforge.service.QuestionViewMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class ExamController {
    private static final String SESSION_KEY = "certforge.exam";

    private final QuestionBankService questionBankService;
    private final PracticeService practiceService;
    private final ExamService examService;
    private final QuestionViewMapper questionViewMapper;

    public ExamController(QuestionBankService questionBankService, PracticeService practiceService,
                          ExamService examService, QuestionViewMapper questionViewMapper) {
        this.questionBankService = questionBankService;
        this.practiceService = practiceService;
        this.examService = examService;
        this.questionViewMapper = questionViewMapper;
    }

    @GetMapping("/exam")
    public String exam(Model model, HttpSession session) {
        if (state(session) != null) {
            return "redirect:/exam/session";
        }
        model.addAttribute("totalQuestions", questionBankService.all().size());
        model.addAttribute("examCounts", List.of(10, 20, 30, 50, 75));
        return "exam-setup";
    }

    @PostMapping("/exam/start")
    public String start(@RequestParam(defaultValue = "10") int number,
                        @RequestParam(defaultValue = "true") boolean shuffleQuestions,
                        @RequestParam(defaultValue = "false") boolean shuffleOptions,
                        HttpSession session) {
        int requested = Math.max(1, Math.min(questionBankService.all().size(), number));
        PracticeService.SelectionCriteria criteria = new PracticeService.SelectionCriteria(null, null, null,
                PracticeService.SelectionType.ALL, false, false, false, requested, shuffleQuestions);
        List<Question> questions = practiceService.select(criteria);
        PracticeSession practiceSession = new PracticeSession(questions.stream().map(Question::id).toList(), shuffleOptions);
        for (Question question : questions) {
            List<String> labels = question.options().stream().map(option -> option.label())
                    .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
            if (shuffleOptions) java.util.Collections.shuffle(labels);
            practiceSession.setOptionOrder(question.id(), labels);
        }
        session.setAttribute(SESSION_KEY, new ExamState(practiceSession));
        return "redirect:/exam/session";
    }

    @GetMapping("/exam/session")
    public String session(Model model, HttpSession session) {
        ExamState state = state(session);
        if (state == null || state.practiceSession().size() == 0) {
            return "redirect:/exam";
        }
        renderSession(model, state);
        return "exam";
    }

    @PostMapping("/exam/save")
    public String save(@RequestParam(name = "answers", required = false) String[] answers,
                       @RequestParam(defaultValue = "next") String direction, HttpSession session) {
        ExamState state = requiredState(session);
        saveAnswer(state, answers);
        if ("previous".equals(direction)) state.practiceSession().previous();
        else state.practiceSession().next();
        return "redirect:/exam/session";
    }

    @PostMapping("/exam/goto")
    public String goTo(@RequestParam int index, HttpSession session) {
        requiredState(session).practiceSession().moveTo(index);
        return "redirect:/exam/session";
    }

    @PostMapping("/exam/flag")
    public String flag(HttpSession session) {
        ExamState state = requiredState(session);
        state.practiceSession().toggleFlag(state.practiceSession().currentQuestionId());
        return "redirect:/exam/session";
    }

    @PostMapping("/exam/submit")
    public String submit(@RequestParam(name = "answers", required = false) String[] answers,
                         HttpSession session, Model model) {
        ExamState state = requiredState(session);
        saveAnswer(state, answers);
        ExamService.ExamResult result = examService.submit(state.practiceSession());
        session.removeAttribute(SESSION_KEY);
        List<ExamAnswerReviewDto> reviews = result.answers().stream()
                .map(answer -> new ExamAnswerReviewDto(
                        questionViewMapper.view(answer.question(), answer.selectedAnswers(), true),
                        answer.selectedAnswers(), questionViewMapper.sorted(answer.question().correctAnswers()),
                        answer.correct(), answer.answered(), questionViewMapper.votes(answer.question().communityVotes())))
                .toList();
        model.addAttribute("examResult", new ExamResultPageDto(result.answers().size(), result.correct(), result.incorrect(),
                result.unanswered(), result.percentage(), result.timeUsedSeconds(), reviews));
        return "exam-result";
    }

    private void renderSession(Model model, ExamState state) {
        Question question = questionBankService.require(state.practiceSession().currentQuestionId());
        Set<String> selected = state.practiceSession().answerFor(question.id());
        QuestionViewDto questionView = questionViewMapper.view(question, selected, false,
                state.practiceSession().optionOrderFor(question.id()));
        model.addAttribute("question", questionView);
        model.addAttribute("selectedAnswers", selected);
        model.addAttribute("position", state.practiceSession().currentIndex() + 1);
        model.addAttribute("total", state.practiceSession().size());
        model.addAttribute("hasPrevious", state.practiceSession().currentIndex() > 0);
        model.addAttribute("hasNext", state.practiceSession().currentIndex() < state.practiceSession().size() - 1);
        model.addAttribute("navigator", navigator(state));
        model.addAttribute("startedAtMillis", state.practiceSession().startedAtMillis());
        model.addAttribute("currentFlagged", state.practiceSession().isFlagged(question.id()));
    }

    private List<NavigatorItemDto> navigator(ExamState state) {
        List<NavigatorItemDto> items = new ArrayList<>();
        for (int index = 0; index < state.practiceSession().size(); index++) {
            Question question = questionBankService.require(state.practiceSession().questionIds().get(index));
            boolean answered = !state.practiceSession().answerFor(question.id()).isEmpty();
            items.add(new NavigatorItemDto(question.questionNumber(), question.id(), answered ? "answered" : "unanswered",
                    index == state.practiceSession().currentIndex(), state.practiceSession().isFlagged(question.id())));
        }
        return items;
    }

    private void saveAnswer(ExamState state, String[] answers) {
        state.practiceSession().answer(state.practiceSession().currentQuestionId(),
                practiceService.normalize(answers == null ? List.of() : List.of(answers)));
    }

    private ExamState state(HttpSession session) {
        Object value = session.getAttribute(SESSION_KEY);
        return value instanceof ExamState examState ? examState : null;
    }

    private ExamState requiredState(HttpSession session) {
        ExamState state = state(session);
        if (state == null) throw new IllegalStateException("No active exam session");
        return state;
    }

    private static final class ExamState {
        private final PracticeSession practiceSession;

        private ExamState(PracticeSession practiceSession) {
            this.practiceSession = practiceSession;
        }

        private PracticeSession practiceSession() { return practiceSession; }
    }
}
