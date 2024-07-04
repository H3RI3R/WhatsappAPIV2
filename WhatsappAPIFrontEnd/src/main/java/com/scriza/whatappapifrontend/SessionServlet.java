package com.scriza.whatappapifrontend;

import java.io.IOException;

import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/session")
public class SessionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        JSONObject jsonResponse = new JSONObject();

        if (session != null && session.getAttribute("username") != null) {
            jsonResponse.put("username", session.getAttribute("username"));
        } else {
            jsonResponse.put("username", "");
        }

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }
}
