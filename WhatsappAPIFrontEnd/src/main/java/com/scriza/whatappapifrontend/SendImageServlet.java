package com.scriza.whatappapifrontend;

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
import java.nio.file.Paths;
import com.scriza.whatappapiBackend.SendImage;

@WebServlet("/send_image")
@MultipartConfig
public class SendImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try {
            String username = request.getParameter("username");
            String recipient = request.getParameter("recipient");
            Part filePart = request.getPart("file");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

            // Save file to local directory
            String uploadDir = "E:\\Chrome Profile Testing\\photoVideo";
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            String filePath = uploadDir + File.separator + fileName;
            Files.copy(filePart.getInputStream(), Paths.get(filePath));

            // Call SendImage to send the file
            SendImage.sendImage(username, recipient, filePath);

            response.getWriter().write("{\"success\":true, \"message\":\"Image sent successfully.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\":false, \"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
