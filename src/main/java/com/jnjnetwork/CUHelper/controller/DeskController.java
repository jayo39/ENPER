package com.jnjnetwork.CUHelper.controller;

import com.jnjnetwork.CUHelper.domain.Question;
import com.jnjnetwork.CUHelper.domain.Schedule;
import com.jnjnetwork.CUHelper.domain.User;
import com.jnjnetwork.CUHelper.service.QuestionService;
import com.jnjnetwork.CUHelper.service.ScheduleService;
import com.jnjnetwork.CUHelper.util.U;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/main")
public class DeskController {

    QuestionService questionService;
    ScheduleService scheduleService;

    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }
    @Autowired
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/attendance")
    public void attendance() {}

    @GetMapping("/feedback")
    public void feedback(Model model) {
        User user = U.getLoggedUser();
        List<Schedule> schedules = scheduleService.findByUserIdAndIsFinished(user.getId(), true);
        model.addAttribute("schedules", schedules);
    }

    @GetMapping("/worksheet")
    public void worksheet(Model model) {
        List<Question> questions = questionService.findAllSortedByBookArPoint();
        model.addAttribute("questions", questions);
    }

}
