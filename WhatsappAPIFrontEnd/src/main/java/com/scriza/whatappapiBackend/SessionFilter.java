package com.scriza.whatappapiBackend;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class SessionFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String loginURI = httpRequest.getContextPath() + "/login.html";
        String loginServletURI = httpRequest.getContextPath() + "/login";
        String logoutServletURI = httpRequest.getContextPath() + "/logout";

        boolean loggedIn = (session != null && session.getAttribute("username") != null);
        boolean loginRequest = httpRequest.getRequestURI().equals(loginURI);
        boolean loginServletRequest = httpRequest.getRequestURI().equals(loginServletURI);
        boolean logoutServletRequest = httpRequest.getRequestURI().equals(logoutServletURI);

        if (loggedIn || loginRequest || loginServletRequest || logoutServletRequest) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(loginURI);
        }
    }

    public void init(FilterConfig fConfig) throws ServletException {}

    public void destroy() {}
}
