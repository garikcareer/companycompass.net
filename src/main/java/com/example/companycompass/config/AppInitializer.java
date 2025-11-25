package com.example.companycompass.config;

import com.example.companycompass.config.db.PersistenceConfig;
import com.example.companycompass.config.demo.SessionCountListener;
import com.example.companycompass.config.demo.SessionLimitFilter;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    private static final Logger logger = LoggerFactory.getLogger(AppInitializer.class);

    /**
     * Configures the servlet context during application startup. Determines the active Spring profile
     * by checking system properties, `application.properties`, or defaults to 'local' if none is specified.
     * It initializes the active profile in the servlet context, adds the necessary listeners, and optionally
     * configures filters based on the profile. Special configuration is applied for the 'demo' profile,
     * enabling session limits and reduced session timeouts.
     *
     * @param servletContext the {@link ServletContext} to be configured during the application startup process
     * @throws ServletException if an error occurs during servlet registration or initialization
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        String activeProfile = "local";
        String systemProperty = System.getProperty("spring.profiles.active");

        if (systemProperty != null && !systemProperty.isEmpty()) {
            activeProfile = systemProperty;
        } else {
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
                if (input != null) {
                    Properties prop = new Properties();
                    prop.load(input);
                    String fileProperty = prop.getProperty("spring.profiles.active");
                    if (fileProperty != null && !fileProperty.isEmpty()) {
                        activeProfile = fileProperty;
                    }
                }
            } catch (IOException ex) {
                logger.warn("Could not find application.properties. Defaulting to 'local'.");
            }
        }

        logger.info("SELECTED SPRING PROFILE: {}", activeProfile);
        servletContext.setInitParameter("spring.profiles.active", activeProfile);

        boolean isDemo = "demo".equalsIgnoreCase(activeProfile);
        servletContext.addListener(new SessionCountListener(isDemo));
        if (isDemo) {
            logger.info("### DEMO MODE: MAX 3 USERS & 30 SECONDS TIMEOUT ENABLED ###");
            FilterRegistration.Dynamic limitFilter = servletContext.addFilter("sessionLimitFilter", new SessionLimitFilter());
            limitFilter.addMappingForUrlPatterns(null, false, "/*");
        }
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        // Load Security and Database configs
        return new Class[] { SecurityConfig.class, PersistenceConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        // Load Web configs
        return new Class[] { WebConfig.class };
    }

    @NonNull
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}