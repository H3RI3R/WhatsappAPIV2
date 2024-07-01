package com.scriza.whatappapiBackend;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/showConnectedNumber")
public class ShowConnectedNumberServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        if (username == null || username.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false, \"message\":\"Username is missing\"}");
            return;
        }
        try {
            String connectedNumber = ShowConnectedNumber.showNumber(username);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"success\":true, \"number\":\"" + connectedNumber + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\":false, \"message\":\"Error occurred\"}");
        }
    }
}
