package com.jnjnetwork.CUHelper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/desk")
public class DeskController {

    @GetMapping("/attendance")
    public void attendance() {}

    @GetMapping("/worksheet")
    public void worksheet() {}

}
