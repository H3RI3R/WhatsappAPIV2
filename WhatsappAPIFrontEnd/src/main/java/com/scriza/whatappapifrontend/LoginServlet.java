package com.scriza.whatappapifrontend;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import com.scriza.whatappapiBackend.DBConnectionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            String authResult = authenticateUser(username, password);
            JSONObject jsonResponse = new JSONObject();

            if ("NOT_REGISTERED".equals(authResult)) {
                jsonResponse.put("message", "Username '" + username + "' is not registered with us.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else if ("WRONG_PASSWORD".equals(authResult)) {
                jsonResponse.put("message", "Password is incorrect.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                jsonResponse.put("scanQR", "http://localhost:8080/WhatsappAPIFrontEnd/scanQR.html");
                jsonResponse.put("message", "You have successfully logged in as " + username);
                response.setStatus(HttpServletResponse.SC_OK);
            }

            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"An error occurred. Please try again.\"}");
        }
    }

    private String authenticateUser(String username, String password) throws SQLException {
        String query = "SELECT password FROM register WHERE username = ?";
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    if (storedPassword.equals(password)) {
                        return "SUCCESS";
                    } else {
                        return "WRONG_PASSWORD";
                    }
                } else {
                    return "NOT_REGISTERED";
                }
            }
        }
    }
}
