package com.scriza.whatappapifrontend;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.scriza.whatappapiBackend.SendMessage; // Assuming SendMessage class is implemented in this package

@WebServlet("/SendMessage")
public class SendMessageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Retrieve recipient number and message from form
        String recipient = request.getParameter("recipient");
        String message = request.getParameter("message");

        // Retrieve username from hidden input
        String username = request.getParameter("username");

        if (username == null || username.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false, \"message\":\"Username is missing\"}");
            return;
        }

        try {
            // Print recipient and message to console (for debugging)
            System.out.println("Username: " + username);
            System.out.println("Recipient: " + recipient);
            System.out.println("Message: " + message);

            // Execute Selenium code to send message on WhatsApp Web
            SendMessage.sendMessage(username, recipient, message);

            // Optional: Provide a response to indicate success
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Message sent successfully to " + recipient);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any errors
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Failed to send message.");
        }
    }
}
