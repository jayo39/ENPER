package com.jnjnetwork.CUHelper.config;

import com.jnjnetwork.CUHelper.exception.CaptchaInvalidException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    private final String DEFAULT_FAILURE_FORWARD_URL = "/user/loginError";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = null;

        if(exception instanceof CaptchaInvalidException) {
            errorMessage = "reCAPTCHA verification failed.";
        }
        else if(exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "Incorrect username or password.";
        }
        else if(exception instanceof DisabledException) {
            errorMessage = "Inactive account. Contact an administrator.";
        }
        else if(exception instanceof CredentialsExpiredException) {
            errorMessage = "Password has expired. Contact an administrator.";
        }
        else {
            errorMessage = "Unknown error. Contact an administrator.";
        }

        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("username", request.getParameter("username"));

        request.getRequestDispatcher(DEFAULT_FAILURE_FORWARD_URL).forward(request, response);
    }
}
