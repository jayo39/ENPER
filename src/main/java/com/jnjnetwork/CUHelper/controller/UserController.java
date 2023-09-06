package com.jnjnetwork.CUHelper.controller;

import com.jnjnetwork.CUHelper.domain.User;
import com.jnjnetwork.CUHelper.service.UserService;
import com.jnjnetwork.CUHelper.util.U;
import com.jnjnetwork.CodeBank.domain.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public void login() {;}

    @PostMapping("/loginError")
    public String loginError(){
        return "user/login";
    }

    @RequestMapping("/rejectAuth")
    public String rejectAuth() {
        return "component/rejectAuth";
    }

    @PostMapping("/register")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> registerOk(@Valid User user, BindingResult result) {
        if (userService.isExist(user.getUsername())) {
            return ResponseEntity.badRequest().body("This username already exists.");
        }

        if (result.hasErrors()) {
            FieldError error = result.getFieldErrors().get(0);
            if (error.getCode().equals("noName")) {
                return ResponseEntity.badRequest().body("Username is required.");
            } else if (error.getCode().equals("noPass")) {
                return ResponseEntity.badRequest().body("Password is required.");
            } else if (error.getCode().equals("noMatch")) {
                return ResponseEntity.badRequest().body("Passwords do not match.");
            }
        }
        userService.register(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/delete")
    @Transactional
    public String deleteOk(@RequestParam("user_id") Long id) {
        User logged_user = U.getLoggedUser();
        userService.removeById(id);
        if(logged_user.getId() == id) {
            return "redirect:/user/logout";
        }
        return "redirect:/admin/panel";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new UserValidator());
    }
}
