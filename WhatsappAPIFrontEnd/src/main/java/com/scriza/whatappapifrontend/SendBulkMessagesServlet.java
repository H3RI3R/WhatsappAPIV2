package com.scriza.whatappapifrontend;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.poi.ss.usermodel.*;

import com.scriza.whatappapiBackend.SendBulkMessages;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/SendBulkMessagesServlet")
@MultipartConfig
public class SendBulkMessagesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        try {
            String username = request.getParameter("username");
            String message = request.getParameter("message");
            Part filePart = request.getPart("excel-file");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

            // Save file to local directory
            String uploadDir = "E:\\Chrome Profile Testing\\uploads";
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            String filePath = uploadDir + File.separator + fileName;
            filePart.write(filePath);

            // Read Excel file and save phone numbers in a list
            List<String> phoneNumbers = new ArrayList<>();
            try (FileInputStream fis = new FileInputStream(filePath);
                 Workbook workbook = WorkbookFactory.create(fis)) {
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    Cell cell = row.getCell(0);
                    if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                        phoneNumbers.add(String.format("%.0f", cell.getNumericCellValue()));
                    } else if (cell != null && cell.getCellType() == CellType.STRING) {
                        phoneNumbers.add(cell.getStringCellValue());
                    }
                }
            }

            // Print the phone numbers
            out.println("Phone Numbers:");
            for (String phoneNumber : phoneNumbers) {
                out.println(phoneNumber);
            }

            // Call the Selenium class to send messages
            try {
                SendBulkMessages.sendMessages(username, phoneNumbers , message);
                out.println("Messages sent successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                out.write("Error sending messages: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("Error processing the request: " + e.getMessage());
        } finally {
            out.close();
        }
    }
}
