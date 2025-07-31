package com.jnjnetwork.CUHelper.exception;

import org.springframework.security.core.AuthenticationException;

public class CaptchaInvalidException extends AuthenticationException {
    public CaptchaInvalidException() {
        super("captcha");   // key for failure handler
    }
}
