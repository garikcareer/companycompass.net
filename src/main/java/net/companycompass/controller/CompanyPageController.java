package net.companycompass.controller;

import net.companycompass.model.Company;
import net.companycompass.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * The CompanyPageController class is responsible for handling HTTP requests
 * related to the Company entity and rendering the appropriate views.
 * It acts as the controller in the MVC architecture, delegating business logic
 * to the CompanyService and preparing data to be displayed in the views.
 */
@Controller
public class CompanyPageController {
    /**
     * Service layer dependency responsible for handling business logic
     * related to Company operations, including creating, reading, updating,
     * and deleting Company entities. This field is used to delegate calls
     * from the controller to the appropriate service methods.
     */
    private final CompanyService companyService;

    /**
     * Represents the Spring Environment abstraction, providing access to
     * the application environment's property sources and active profiles.
     * This object can be used to retrieve configuration properties,
     * verify active profiles, and manage property source property resolution.
     * It supports configurations for different environments, such as
     * development, production, or test.
     * In this context, it is used to check the currently active profiles
     * (e.g., to determine if the application is running in "demo" mode).
     */
    private final Environment environment;

    /**
     * Constructs a new instance of the CompanyPageController class. Initializes the controller
     * with a CompanyService, which serves as the core service layer dependency for managing
     * Company-related business logic.
     *
     * @param companyService the service layer dependency responsible for handling operations
     *                       related to Company entities, including create, read, update,
     *                       and delete operations.
     */
    @Autowired
    public CompanyPageController(CompanyService companyService, Environment environment) {
        this.companyService = companyService;
        this.environment = environment;
    }

    /**
     * Retrieves all companies and prepares a view for displaying them.
     * The method sets up a ModelAndView object with the layout template and populates it with
     * the content to be displayed, the page title, and the list of companies.
     *
     * @return a ModelAndView object containing the layout template, page content, page title,
     * and the list of companies to be displayed
     */
    @GetMapping("/")
    public ModelAndView getAllCompanies() {
        ModelAndView mav = new ModelAndView("layout");
        mav.addObject("content", "company");
        mav.addObject("pageTitle", "Companies");
        mav.addObject("companies", companyService.getCompanies());
        return mav;
    }

    /**
     * Displays the "Add Company" form by preparing a ModelAndView object.
     * The method sets up the layout template and includes a blank Company object
     * for initializing the form, along with the required views and page title.
     *
     * @return a ModelAndView object containing the layout template, a blank Company object,
     * content view for the "Add Company" page, and the corresponding page title
     */
    @GetMapping("/add")
    public ModelAndView addCompany() {
        ModelAndView mav = new ModelAndView("layout");
        mav.addObject("content", "company-form");
        mav.addObject("pageTitle", "Add Company");
        mav.addObject("company", new Company());
        return mav;
    }

    /**
     * Retrieves a specific company by its ID and prepares a view for editing the company's details.
     * The method sets up a ModelAndView object with the layout template, a content view
     * for the "Edit Company" page, the corresponding page title, and the company details.
     *
     * @param id the unique identifier of the company to be edited
     * @return a ModelAndView object containing the layout template, company details,
     * content view for the "Edit Company" page, and the corresponding page title
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editCompany(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("layout");
        mav.addObject("content", "company-form");
        mav.addObject("pageTitle", "Edit Company");
        mav.addObject("company", companyService.getCompanyById(id));
        return mav;
    }

    /**
     * Saves the details of a company by either adding it as a new entry or updating an existing one.
     * If the `id` field of the provided company is null, the company is treated as a new entity,
     * otherwise, the existing company with the same `id` is updated.
     *
     * @param company the Company object containing the details to be saved or updated
     * @return a String indicating the redirection path to the homepage after saving or updating the company
     */
    @PostMapping("/save")
    public String saveCompany(@ModelAttribute Company company) {
        if (company.getId() == null) {
            companyService.addCompany(company);
        } else {
            companyService.updateCompany(company);
        }
        return "redirect:/";
    }

    /**
     * Deletes a company based on the provided unique identifier and redirects to the homepage.
     *
     * @param id the unique identifier of the company to be deleted
     * @return a String indicating the redirection path to the homepage after the deletion is performed
     */
    @GetMapping("/delete/{id}")
    public String deleteCompany(@PathVariable("id") Long id) {
        companyService.deleteCompany(id);
        return "redirect:/";
    }

    /**
     * Displays the "About" page by preparing a ModelAndView object.
     * The method sets up the layout template and includes the content view
     * and page title for the "About" page.
     *
     * @return a ModelAndView object containing the layout template,
     * content view for the "About" page, and the corresponding page title
     */
    @GetMapping("/about")
    public ModelAndView about() {
        ModelAndView mav = new ModelAndView("layout");
        mav.addObject("content", "about");
        mav.addObject("pageTitle", "About");
        return mav;
    }

    /**
     * Displays the "Contact" page by preparing a ModelAndView object.
     * The method sets up the layout template and includes the content view
     * and page title for the "Contact" page.
     *
     * @return a ModelAndView object containing the layout template,
     * content view for the "Contact" page, and the corresponding page title
     */
    @GetMapping("/contact")
    public ModelAndView contact() {
        ModelAndView mav = new ModelAndView("layout");
        mav.addObject("content", "contact");
        mav.addObject("pageTitle", "Contact");
        return mav;
    }

    @ModelAttribute("isDemoMode")
    public boolean isDemoMode() {
        // Returns true if "demo" profile is active
        return environment.acceptsProfiles(Profiles.of("demo"));
    }
}