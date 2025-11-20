package com.example.companycompass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

/**
 * WebConfig is a configuration class that sets up the web application's MVC context,
 * registering various beans and enabling essential functionality for the application.
 * This class implements the {@link WebMvcConfigurer} interface and is annotated with
 * {@code @Configuration}, {@code @EnableWebMvc}, and {@code @ComponentScan}.
 * The primary responsibilities of this class are:
 * - Configuring Thymeleaf template resolution and rendering.
 * - Setting up a view resolver for processing Thymeleaf templates.
 * - Defining resource handlers for serving static resources.
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.example.companycompass.controller")
public class WebConfig implements WebMvcConfigurer {
    /**
     * Configures and provides a {@link SpringResourceTemplateResolver} bean for resolving Thymeleaf templates.
     * This resolver is pre-configured with a template location prefix, suffix, and template mode to
     * work with HTML files stored in the `/templates/` directory on the classpath.
     *
     * @return an instance of {@link SpringResourceTemplateResolver} configured to resolve HTML templates.
     */
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        return resolver;
    }

    /**
     * Configures and provides a {@link SpringTemplateEngine} bean for processing Thymeleaf templates.
     * The {@link SpringTemplateEngine} is set up with a {@link SpringResourceTemplateResolver}
     * for resolving template files such as HTML in the application's resources directory.
     *
     * @return an instance of {@link SpringTemplateEngine} configured with a template resolver
     *         to enable template rendering for the application.
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    /**
     * Configures and provides a {@link ThymeleafViewResolver} bean used to resolve and render Thymeleaf templates.
     * The view resolver is configured with a {@link SpringTemplateEngine} for processing templates
     * and a UTF-8 character encoding to ensure proper handling of internationalized content.
     *
     * @return an instance of {@link ThymeleafViewResolver} configured with a template engine and UTF-8 encoding.
     */
    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    /**
     * Configures resource handlers to serve static resources such as CSS, JavaScript, and other files
     * from specific locations in the classpath. This method maps specific URL patterns to their
     * corresponding resource locations.
     *
     * @param registry the {@link ResourceHandlerRegistry} used to register resource handlers.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/", "classpath:/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/", "classpath:/js/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
