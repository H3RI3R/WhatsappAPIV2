package com.scriza.whatappapifrontend;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.scriza.whatappapiBackend.DBConnectionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetching data from both query parameters and form data
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String phone_number = request.getParameter("phone_number");
        String email = request.getParameter("email");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        try {
            if (isUsernameTaken(username)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("message", "Username '" + username + "' is already taken.");
            } else if (isPhoneNumberTaken(phone_number)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("message", "Phone Number '" + phone_number + "' is already taken.");
            } else {
                registerUser(username, password, phone_number, email);
                response.setStatus(HttpServletResponse.SC_OK);
                jsonResponse.put("message", "User " + username + " has been successfully registered.");
                jsonResponse.put("login_url", "http://localhost:8080/WhatsappAPIFrontEnd/login.html");
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("message", "An error occurred while processing the request.");
            e.printStackTrace();
        }

        out.print(jsonResponse.toString());
        out.flush();
    }

    private boolean isPhoneNumberTaken(String phone_number) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM register WHERE phone_number = ?";
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, phone_number);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0; // Return true if phone number already exists
                }
            }
        }
        return false; // Default to false if an error occurs
    }

    private boolean isUsernameTaken(String username) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM register WHERE username = ?";
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0; // Return true if username already exists
                }
            }
        }
        return false; // Default to false if an error occurs
    }

    private void registerUser(String username, String password, String phone_number, String email) throws SQLException {
        String query = "INSERT INTO register (username, password, phone_number, email) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, phone_number);
            preparedStatement.setString(4, email);
            preparedStatement.executeUpdate();
        }
    }
}
