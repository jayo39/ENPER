package com.jnjnetwork.CUHelper.controller;

import com.jnjnetwork.CUHelper.domain.User;
import com.jnjnetwork.CUHelper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/panel")
    public void panel(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
    }
}
