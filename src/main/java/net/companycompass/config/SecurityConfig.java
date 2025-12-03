package net.companycompass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


/**
 * SecurityConfig is a Spring Security Configuration class responsible for defining the security
 * configurations for the application. It is annotated with {@code @Configuration} and
 * {@code @EnableWebSecurity} to ensure that Spring Security is enabled and is configured
 * based on the customization provided in this class.
 * The class defines beans and configurations necessary for securing the application,
 * including the configuration of HTTP security rules and the registration of a custom
 * MVC handler mapping introspector.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Creates and registers a bean named "mvcHandlerMappingIntrospector" of type HandlerMappingIntrospector.
     * This bean provides access to the configured HandlerMapping instances in a Spring MVC application.
     *
     * @return an instance of HandlerMappingIntrospector to introspect the mappings of the registered handler mappings.
     */
    @Bean(name = "mvcHandlerMappingIntrospector")
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

    /**
     * Configures and returns a {@link SecurityFilterChain} bean for setting up HTTP security configurations.
     * This method defines access rules for various endpoints, disabling CSRF protection and specifying which
     * requests are permitted or require authentication.
     *
     * @param http         the {@link HttpSecurity} object used to customize security configurations.
     * @return a configured {@link SecurityFilterChain} instance representing the security filter chain.
     * @throws Exception if an error occurs while building the security filter chain configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/", "/add", "/save", "/edit/**", "/delete/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}