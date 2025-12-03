package net.companycompass.config.demo;

import java.util.concurrent.atomic.AtomicInteger;

public class ActiveUserStore {
    /**
     * Tracks the number of currently active user sessions in the application.
     * This variable is used to manage concurrency and enforce session limits
     * within the application, particularly in demo environments. The count is
     * incremented whenever a new session starts and decremented when a session ends.
     */
    public static final AtomicInteger activeSessions = new AtomicInteger(0);
}