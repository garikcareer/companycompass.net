package net.companycompass.config.demo;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class SessionLimitFilter implements Filter {
    private static final int MAX_SESSIONS = 3;

    /**
     * Filters incoming HTTP requests to enforce a limit on the number of active sessions.
     * If the number of active sessions exceeds the predefined maximum and the current
     * request does not have an active session, the server responds with a 503 status
     * and an appropriate message. Otherwise, the request is passed down the filter chain.
     *
     * @param request  the incoming request object, expected to be of type {@link HttpServletRequest}
     * @param response the outgoing response object, expected to be of type {@link HttpServletResponse}
     * @param chain    the filter chain used to propagate the request if the session limit is not exceeded
     * @throws IOException      if an input or output error occurs during filtering
     * @throws ServletException if a servlet-specific error occurs during filtering
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        if (session == null && ActiveUserStore.activeSessions.get() >= MAX_SESSIONS) {
            httpResponse.setStatus(503);
            httpResponse.setContentType("text/html");
            httpResponse.getWriter().write(
                    "<html><body style='font-family: sans-serif; text-align: center; padding-top: 50px;'>" +
                            "<h1 style='color: #e74c3c;'>Server Busy</h1>" +
                            "<p>This demo running on a server is currently at full capacity.</p>" +
                            "<p>Please wait a few minutes for a slot to open up.</p>" +
                            "<button onclick='location.reload()' style='padding: 10px 20px; cursor: pointer;'>Try Again</button>" +
                            "</body></html>"
            );
            return;
        }
        chain.doFilter(request, response);
    }
}