package com.scriza.whatappapifrontend;

import com.scriza.whatappapiBackend.SendSticker;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/send_sticker")
@MultipartConfig
public class SendStickerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String recipient = request.getParameter("recipient");
        Part filePart = request.getPart("stickerFile");

        // Check if filePart is null
        if (filePart == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\":\"File upload failed: No file received.\"}");
            return;
        }

        // Save the file to a temporary location
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        Path filePath = Paths.get("E:/Chrome Profile Testing/stickers").resolve(fileName);

        // Create directories if they do not exist
        Files.createDirectories(filePath.getParent());

        try {
            // Save the uploaded file
            Files.copy(filePart.getInputStream(), filePath);
            
            // Send the sticker using Selenium
            SendSticker.sendSticker(username, recipient, filePath.toString());

            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Sticker sent successfully!\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\":\"Failed to send sticker. Error: " + e.getMessage() + "\"}");
        } finally {
            // Clean up the temporary file
            Files.deleteIfExists(filePath);
        }
    }
}
