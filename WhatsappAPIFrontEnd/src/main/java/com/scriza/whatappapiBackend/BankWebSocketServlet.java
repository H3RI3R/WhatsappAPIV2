package com.scriza.whatappapiBackend;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/startQrScanning") // Endpoint URL to trigger QR scanning
public class BankWebSocketServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            BankWebSocketServer.startQrScanning(); // Call method to start QR scanning
            response.getWriter().println("QR scanning started successfully.");
        } catch (InterruptedException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error starting QR scanning.");
        }
    }
}
