package com.jnjnetwork.CUHelper.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {
    @GetMapping("/checkSession")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<String> checkSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity.ok("{\"status\": \"valid\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"status\": \"invalid\"}");
        }
    }
}
