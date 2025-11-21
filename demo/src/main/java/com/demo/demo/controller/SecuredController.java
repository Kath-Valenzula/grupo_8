package com.demo.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SecuredController {

    @GetMapping("/greetings")
    public String greetings(@RequestParam(value="name", defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greetings";
    }
}