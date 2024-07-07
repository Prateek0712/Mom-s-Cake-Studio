package com.pratik.major.controller;

import com.pratik.major.model.Roles;
import com.pratik.major.model.User;
import com.pratik.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login(){
        return "/login";
    }

    @GetMapping("/register")
    public String  registerGet()
    {
        return  "register";
    }

    @PostMapping("/register")
    public String  registerPost(@ModelAttribute("user") User user, HttpServletRequest request) throws ServletException {
        String password=user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRole(Roles.USER);
        userRepository.save(user);
        request.login(user.getEmail(),password);
        return "redirect:/";
    }

}
