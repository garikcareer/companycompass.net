package net.companycompass;

import net.companycompass.config.SecurityConfig;
import net.companycompass.config.WebConfig;
import net.companycompass.config.db.PersistenceConfig;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;

/**
 * The entry point for the embedded Tomcat server.
 * Responsible for bootstrapping the server and loading the Spring Application Context.
 */
public class CompanyCompassApplication {
    private static final Logger logger = LoggerFactory.getLogger(CompanyCompassApplication.class);
    private static final int PORT = 8080;

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(System.getProperty("java.io.tmpdir") + File.separator + "tomcat." + PORT);
        tomcat.setPort(PORT);
        tomcat.getConnector();

        String contextPath = System.getenv("APP_CONTEXT_PATH");
        if (contextPath == null || contextPath.isEmpty()) {
            contextPath = "";
        }
        String appUrl = "http://localhost:" + PORT + contextPath;
        logger.info("APP CONTEXT PATH: '{}'", appUrl);

        File docBase = new File("src/main/webapp");
        if (!docBase.exists()) {
            docBase = new File(".");
        }

        Context context = tomcat.addWebapp(contextPath, docBase.getAbsolutePath());

        AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();
        springContext.register(SecurityConfig.class, PersistenceConfig.class, WebConfig.class);
        DispatcherServlet dispatcherServlet = new DispatcherServlet(springContext);
        Tomcat.addServlet(context, "dispatcherServlet", dispatcherServlet);
        context.addServletMappingDecoded("/*", "dispatcherServlet");

        tomcat.start();

        try {
            if (contextPath.isEmpty()) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(appUrl));
                }
            }
        } catch (Exception e) {
            logger.error("Failed to open browser", e);
        }
        tomcat.getServer().await();
    }
}