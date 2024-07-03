package com.scriza.whatappapifrontend;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.scriza.whatappapiBackend.SendContact;

import java.io.IOException;

@WebServlet("/send_contact")
public class SendContactServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try {
            String username = request.getParameter("username");
            String recipient = request.getParameter("recipient");
            String contactNumber = request.getParameter("contactNumber");
            String contactName = request.getParameter("contactName");

            // Call SendContact to send the contact information
            SendContact.sendContact(username, recipient, contactNumber, contactName);

            response.getWriter().write("{\"success\":true, \"message\":\"Contact sent successfully.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\":false, \"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
