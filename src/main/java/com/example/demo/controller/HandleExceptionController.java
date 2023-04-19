package com.example.demo.controller;

import com.example.demo.exception.UserException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandleExceptionController {
    @ExceptionHandler(UserException.class)
    public String handleUserException(Model model, UserException exception){
        model.addAttribute("error", exception.getMessage());
        return "error";
    }
}
