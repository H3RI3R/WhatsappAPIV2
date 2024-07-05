package com.scriza.whatappapifrontend;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.scriza.whatappapiBackend.SendIndividualMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/SendIndividualMessagesServlet")
@MultipartConfig
public class SendIndividualMessagesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        try {
            String username = request.getParameter("username");
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

            // Read Excel file and populate the map
            Map<String, String> phoneNumberMessageMap = new HashMap<>();
            DataFormatter formatter = new DataFormatter();
            try (FileInputStream fis = new FileInputStream(filePath);
                 Workbook workbook = WorkbookFactory.create(fis)) {
                Sheet sheet = workbook.getSheetAt(0);
                boolean firstRow = true;
                for (Row row : sheet) {
                    if (firstRow) {
                        firstRow = false;
                        continue; // Skip the first row (headers)
                    }
                    Cell phoneCell = row.getCell(0);
                    Cell messageCell = row.getCell(1);

                    if (phoneCell != null && messageCell != null) {
                        String phoneNumber = formatter.formatCellValue(phoneCell).trim();
                        String message = formatter.formatCellValue(messageCell).trim();
                        phoneNumberMessageMap.put(phoneNumber, message);
                    }
                }
            }

            // Print the phone numbers and messages
            out.println("Phone Numbers and Messages:");
            for (Map.Entry<String, String> entry : phoneNumberMessageMap.entrySet()) {
                out.println(entry.getKey() + " - " + entry.getValue());
            }

            // Call the Selenium class to send messages
            try {
                SendIndividualMessage.sendMessages(username, phoneNumberMessageMap);
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
