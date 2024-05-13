package com.example.onlinebookstore.controller;

import com.example.onlinebookstore.dao.BookAuthorDAO;
import com.example.onlinebookstore.dao.BookCategoryDAO;
import com.example.onlinebookstore.formsdata.UserProfileFormData;
import com.example.onlinebookstore.model.BookAuthor;
import com.example.onlinebookstore.service.UserProfile.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.onlinebookstore.model.User;
import com.example.onlinebookstore.service.User.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    UserProfileService userProfileService;
    @Autowired
    UserService userService;
    @Autowired
    BookAuthorDAO bookAuthorDAO;
    @Autowired
    BookCategoryDAO bookCategoryDAO;

    @RequestMapping("/login")
    public String login(){
        return "auth/signin";
    }

    @RequestMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "auth/signup";
    }

    @RequestMapping("/save")
    public String registerUser(@ModelAttribute("user") User user, Model model){
       
        if(userService.isUserPresent(user)){
            model.addAttribute("successMessage", "User already registered!");
            return "auth/signin";
        }
        model.addAttribute("successMessage", "User registered successfully!");
        userService.saveUser(user);
        model.addAttribute("userprofile",new UserProfileFormData());
        model.addAttribute("authors", bookAuthorDAO.findAll());
        model.addAttribute("categories",bookCategoryDAO.findAll());
        return "/user/profile";
    }
}
