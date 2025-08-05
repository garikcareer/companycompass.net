package com.example.site;

import com.example.data.config.JPAConfig;
import com.example.site.config.ApplicationConfig;
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

@Configuration
public class Application implements WebMvcConfigurer {
  private static final String HOST = "localhost";
  private static final int PORT = 8080;
  private static final String WEBAPP_DIR = "src/main/webapp/";
  private static final String CATALINA_BASE = "build/tomcat";
  private static final String CONTEXT_PATH = "";

  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) throws LifecycleException {
    Tomcat tomcat = new Tomcat();
    tomcat.setBaseDir(CATALINA_BASE);
    tomcat.setHostname(HOST);
    tomcat.setPort(PORT);
    tomcat.getHost().setAppBase(".");

    System.setProperty("catalina.base", CATALINA_BASE);
    Context context = tomcat.addWebapp(CONTEXT_PATH, new File(WEBAPP_DIR).getAbsolutePath());

    AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
    applicationContext.register(ApplicationConfig.class, JPAConfig.class);

    DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
    Tomcat.addServlet(context, "dispatcherServlet", dispatcherServlet);
    context.addServletMappingDecoded("/*", "dispatcherServlet");

    tomcat.getConnector();
    tomcat.start();
    logger.info("Web Application is started on http://{}:{}", HOST, PORT);
    tomcat.getServer().await();
  }

  @Bean
  public ThymeleafViewResolver thymeleafViewResolver(SpringTemplateEngine templateEngine) {
    var resolver = new org.thymeleaf.spring6.view.ThymeleafViewResolver();
    resolver.setTemplateEngine(templateEngine);
    resolver.setCharacterEncoding("UTF-8");
    resolver.setOrder(1);
    resolver.setViewNames(new String[]{"*"});
    return resolver;
  }

  @Bean
  public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine engine = new SpringTemplateEngine();
    engine.setTemplateResolver(templateResolver());
    return engine;
  }

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

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**")
            .addResourceLocations("/static/");
  }
}
