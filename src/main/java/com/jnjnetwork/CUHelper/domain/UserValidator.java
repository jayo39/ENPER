package com.jnjnetwork.CodeBank.domain;

import com.jnjnetwork.CUHelper.domain.User;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "noName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "noPass");

        if(!user.getPassword().equals(user.getRe_password())){
            errors.rejectValue("re_password", "noMatch");
        }
    }
}