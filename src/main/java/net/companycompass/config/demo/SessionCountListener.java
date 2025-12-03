package net.companycompass.config.demo;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record SessionCountListener(boolean isDemoMode) implements HttpSessionListener {
    private static final Logger logger = LoggerFactory.getLogger(SessionCountListener.class);

    /**
     * Handles the event triggered when a new HTTP session is created.
     * Updates the count of active sessions and configures the session timeout if in demo mode.
     *
     * @param se the HttpSessionEvent containing the session information
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int current = ActiveUserStore.activeSessions.incrementAndGet();
        logger.info(">>> NEW SESSION CREATED. Active Users: {}", current);
        if (isDemoMode) {
            se.getSession().setMaxInactiveInterval(15);
        }
    }

    /**
     * Handles the event triggered when an HTTP session is destroyed.
     * Decreases the count of active sessions and logs the updated count.
     *
     * @param se the HttpSessionEvent containing the session information
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int current = ActiveUserStore.activeSessions.decrementAndGet();
        logger.info("<<< SESSION ENDED. Active Users: {}", current);
    }
}