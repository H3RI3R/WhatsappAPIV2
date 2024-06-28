package com.scriza.whatappapiBackend;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/startQrScanning") // Endpoint URL to trigger QR scanning
public class BankWebSocketServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        if (username == null || username.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username is required.");
            return;
        }

        try {
            BankWebSocketServer.startQrScanning(username); // Pass username to startQrScanning method
            response.getWriter().println("QR scanning started successfully for user: " + username);
        } catch (InterruptedException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error starting QR scanning.");
        }
    }
}
