package com.example.companycompass.controller;

import com.example.companycompass.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CompanyPageController {
    private final CompanyService companyService;

    @Autowired
    public CompanyPageController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("layout");
        modelAndView.addObject("content", "index");
        modelAndView.addObject("pageTitle", "Home");
        return modelAndView;
    }

    @GetMapping("/")
    @ModelAttribute
    public ModelAndView getAllCompanies(Model model) {
        model.addAttribute("content", "company");
        model.addAttribute("pageTitle", "Companies");
        model.addAttribute("companyList", companyService.getCompanies());
        return new ModelAndView("layout");
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
