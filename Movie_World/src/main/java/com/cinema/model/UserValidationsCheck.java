/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinema.model;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author Ruchit Gandhi <Ruchit at Northeasten.com>
 */
public class UserValidationsCheck implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(User.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        if (user.getUserRole() == null) {
            errors.rejectValue("userRole", "error.invalid.userRole", "invalid userRole");
            return;
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "error.invalid.userName", Constants.Message.REQUIRED_USERNAME.getDayInfo());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.invalid.password", Constants.Message.REQUIRED_PASSWORD.getDayInfo());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "error.invalid.firstName", Constants.Message.REQUIRED_FIRSTNAME.getDayInfo());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "error.invalid.lastName", Constants.Message.REQUIRED_LASTNAME.getDayInfo());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "error.invalid.email", Constants.Message.REQUIRED_EMAIL.getDayInfo());

    }
}
