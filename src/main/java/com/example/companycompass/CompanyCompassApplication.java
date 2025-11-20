package com.example.companycompass;

import com.example.companycompass.config.db.PersistenceConfig;
import com.example.companycompass.config.WebConfig;
import com.example.companycompass.config.SecurityConfig;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.io.File;

/**
 * The CompanyCompassApplication class is the entry point for the application.
 * It configures and starts an embedded Apache Tomcat server with customized
 * Spring MVC configuration, including resource handling and Thymeleaf template integration.
 * This class implements the {@link WebMvcConfigurer} interface to customize
 * Spring MVC configurations such as resource handler registration.
 * Features:
 * - Starts an embedded Tomcat server configured with a specified host, port,
 * and context path.
 * - Configures the application context with security, persistence, and web configurations.
 * - Sets up Thymeleaf as the view resolver for rendering HTML templates.
 * - Registers static resource handlers to serve static assets.
 */
@Configuration
public class CompanyCompassApplication implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(CompanyCompassApplication.class);
    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static final String WEBAPP_DIR = ".";
    private static final String CATALINA_BASE = "build/tomcat";
    private static final String CONTEXT_PATH = "";

    /**
     * The main method initializes and starts the embedded Tomcat server for the application.
     * It sets up server configurations, web application settings, and integrates the Spring
     * application context with a DispatcherServlet for handling incoming requests.
     *
     * @param args command-line arguments passed to the program.
     * @throws LifecycleException if an error occurs during the lifecycle of the Tomcat server.
     */
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(CATALINA_BASE);
        tomcat.setHostname(HOST);
        tomcat.setPort(PORT);
        tomcat.getHost().setAppBase(".");
        tomcat.getConnector();

        System.setProperty("catalina.base", CATALINA_BASE);
        Context context = tomcat.addWebapp(CONTEXT_PATH, new File(WEBAPP_DIR).getAbsolutePath());

        AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();
        springContext.register(SecurityConfig.class, PersistenceConfig.class, WebConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(springContext);
        Tomcat.addServlet(context, "dispatcherServlet", dispatcherServlet);
        context.addServletMappingDecoded("/*", "dispatcherServlet");

        tomcat.start();
        logger.info("CompanyCompass Application is started on http://{}:{}", HOST, PORT);
        tomcat.getServer().await();
    }

    /**
     * Configures and creates a ThymeleafViewResolver bean for rendering views
     * based on Thymeleaf templates.
     *
     * @param templateEngine the view resolver to use the SpringTemplateEngine instance
     *                       for processing templates.
     * @return a ThymeleafViewResolver instance configured with the provided template engine,
     * character encoding, order, and view name patterns.
     */
    @Bean
    public ThymeleafViewResolver thymeleafViewResolver(SpringTemplateEngine templateEngine) {
        var resolver = new org.thymeleaf.spring6.view.ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(1);
        resolver.setViewNames(new String[]{"*"});
        return resolver;
    }

    /**
     * Configures and returns a {@link SpringTemplateEngine} bean for processing Thymeleaf templates.
     * The {@link SpringTemplateEngine} utilizes the configured {@link SpringResourceTemplateResolver}
     * to resolve and process template files.
     *
     * @return a configured instance of {@link SpringTemplateEngine} for rendering Thymeleaf views.
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    /**
     * Configures and returns a {@link SpringResourceTemplateResolver} bean for resolving Thymeleaf template files.
     * The resolver is responsible for locating and processing template files with the specified prefix, suffix,
     * template mode, character encoding, and caching settings.
     *
     * @return a configured {@link SpringResourceTemplateResolver} instance for resolving Thymeleaf templates.
     */
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);
        return resolver;
    }

    /**
     * Adds resource handlers to serve static resources such as CSS, JavaScript, and images
     * from specific locations.
     *
     * @param registry the {@link ResourceHandlerRegistry} used to register resource handlers
     *                 and specify their resource locations.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/");
    }
}
