package com.example.onlinebookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

    @RequestMapping("/admin/main_menu")
    public String getAdminHome(){
        return "/admin/main_menu";
    }
}
