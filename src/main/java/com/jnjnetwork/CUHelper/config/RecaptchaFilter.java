package com.jnjnetwork.CUHelper.config;

import com.jnjnetwork.CUHelper.exception.CaptchaInvalidException;
import com.jnjnetwork.CUHelper.service.RecaptchaService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RecaptchaFilter extends OncePerRequestFilter {

    private final RecaptchaService recaptchaService;
    private final AuthenticationFailureHandler failureHandler;

    public RecaptchaFilter(RecaptchaService recaptchaService, CustomLoginFailureHandler failureHandler) {
        this.recaptchaService = recaptchaService;
        this.failureHandler = failureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        boolean isLoginPost = "/user/login".equals(req.getServletPath())
                && "POST".equalsIgnoreCase(req.getMethod());

        if (isLoginPost) {
            String token = req.getParameter("g-recaptcha-response");
            if (!recaptchaService.isHuman(token, req.getRemoteAddr())) {
                failureHandler.onAuthenticationFailure(
                        req, res, new CaptchaInvalidException());
                return;
            }
        }
        chain.doFilter(req, res);
    }
}
