package com.example.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("layout");
        modelAndView.addObject("content", "index");
        modelAndView.addObject("pageTitle", "Home");
        return modelAndView;
    }

    @GetMapping("/web/company/add")
    public ModelAndView  addCompany() {
        ModelAndView modelAndView = new ModelAndView("layout");
        modelAndView.addObject("content", "addcompany");
        modelAndView.addObject("pageTitle", "Add Company");
        return modelAndView;
    }

    @GetMapping("/about")
    @ModelAttribute("layout")
    public ModelAndView  about() {
        ModelAndView modelAndView = new ModelAndView("layout");
        modelAndView.addObject("content", "about");
        modelAndView.addObject("pageTitle", "About");
        return modelAndView;
    }

    @GetMapping("/contact")
    @ModelAttribute("layout")
    public ModelAndView  contact() {
        ModelAndView modelAndView = new ModelAndView("layout");
        modelAndView.addObject("content", "contact");
        modelAndView.addObject("pageTitle", "Contact");
        return modelAndView;
    }
}
