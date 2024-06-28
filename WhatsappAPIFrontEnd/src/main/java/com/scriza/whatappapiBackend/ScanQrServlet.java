//package com.scriza.whatappapiBackend;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebServlet("/startQrScanning")
//public class ScanQrServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String username = request.getParameter("username");
//        if (username != null && !username.isEmpty()) {
//            System.out.println("Username from URL: " + username);
//            // Start the QR scanning process
//            new Thread(() -> {
//                try {
//                    BankWebSocketServer.startQrScanning(username); // Correctly pass username here
//                } catch (IOException | InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//            response.getWriter().write("QR Scanning started for user: " + username);
//        } else {
//            response.getWriter().write("Username parameter is missing or empty in the URL.");
//        }
//    }
//}
