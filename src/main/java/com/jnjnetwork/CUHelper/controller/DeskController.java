package com.jnjnetwork.CUHelper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/desk")
public class DeskController {

    @GetMapping("/enroll")
    public void enroll(Model model) {
        model.addAttribute("nav", "nav-desk-enroll");
    }

    @GetMapping("/attendance")
    public void attendance(Model model) {
        model.addAttribute("nav", "nav-desk-attendance");
    }

}
