package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.UserException;
import com.example.demo.request.RegiterRequest;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
//import com.example.demo.exception.UserException;
import com.example.demo.request.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("login")
    public String showLogin(Model model) {
        model.addAttribute("loginRequest", new LoginRequest("", ""));
        return "User/login";
    }

    @PostMapping("login")
    public String handleLogin(@Valid @ModelAttribute("loginRequest") LoginRequest loginRequest, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "User/login";
        }
        User user;
        try {
            user = userService.login(loginRequest.getAccountName(), loginRequest.getPassword());
            session.setAttribute("user", new UserDTO(user.getId(),user.getFirstName(),user.getLastName(), user.getAccountName(), user.getEmail(),user.getRole()));
            return "redirect:/";
        } catch (UserException e) {
            switch (e.getMessage()) {
                case "User is not found":
                    bindingResult.addError(new FieldError("loginRequest", "accountName", "User is not found"));
                    break;
                case "User is not active":
                    bindingResult.addError(new FieldError("loginRequest", "accountName", "User is not active"));
                    break;
                case "Password is incorrect":
                    bindingResult.addError(new FieldError("loginRequest", "password", "Password is incorrect"));
                    break;
            }
        }
        return "User/login";
    }

    @GetMapping("register")
    public String showRegister(Model model) {
        model.addAttribute("registerRequest", new RegiterRequest("", "", "","",""));
        return "User/register";
    }

    @PostMapping("register")
    public String handleRegister(@Valid @ModelAttribute("registerRequest") RegiterRequest registerRequest, BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "User/register";
        }
        User user;
        try {
            user = userService.register(registerRequest.getAccountName(),registerRequest.getFirstName(),registerRequest.getLastName(), registerRequest.getEmail(), registerRequest.getPassword());
            session.setAttribute("user", new UserDTO(user.getId(),user.getFirstName(),user.getLastName(), user.getAccountName(), user.getEmail(),user.getRole()));

            return "User/verifyUser"; // Trả về view "User/verifyUser" nếu không có lỗi xảy ra
        } catch (Exception e) {
            switch (e.getMessage()) {
                case "Username already exists":
                    bindingResult.addError(new FieldError("registerRequest", "accountName", "Username already exists"));
                    break;
                case "Email already exists":
                    bindingResult.addError(new FieldError("registerRequest", "email", "Email already exists"));
                    break;
            }
        }

        return "User/register"; // Trả về view "User/register" nếu có lỗi xảy ra
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping("verifyUser")
    public String verifyUser(Model model) {
        return "User/verifyUser";
    }

    @PostMapping("verifyUser")
    public String handleVerifyUser(@RequestParam("verifyUser") String verifyUser, HttpSession session, Model model) {
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        try {
            userService.verifyUser(userDTO.getAccountName(), verifyUser);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "User/verifyUser";

    }
}
