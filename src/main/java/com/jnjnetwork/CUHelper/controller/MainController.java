package com.jnjnetwork.CUHelper.controller;

import com.jnjnetwork.CUHelper.domain.Question;
import com.jnjnetwork.CUHelper.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/main")
public class MainController {

    QuestionService questionService;

    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/attendance")
    public void attendance() {}

    @GetMapping("/worksheet")
    public void worksheet(Model model) {
        List<Question> questions = questionService.findAllSortedByBookArPoint();
        model.addAttribute("questions", questions);
    }

}
