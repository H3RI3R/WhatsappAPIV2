package com.scriza.whatappapifrontend;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import org.json.JSONObject;

@WebServlet("/checkStatus")
public class CheckStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String PROFILE_DIRECTORY = "E:\\Chrome Profile Testing";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        JSONObject jsonResponse = new JSONObject();

        if (username == null || username.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username is required.");
            return;
        }

        File userProfileDir = new File(PROFILE_DIRECTORY + "\\" + username);
        if (userProfileDir.exists() && userProfileDir.isDirectory()) {
            jsonResponse.put("status", "active");
        } else {
            jsonResponse.put("status", "closed");
        }

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }
}
