package com.scriza.whatappapiBackend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;

public class ShowConnectedNumber {

    public static String showNumber(String username) throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:/selenium WebDriver/chromedriver-win64/chromedriver.exe");

        String chromeProfilePath = "E:\\Chrome Profile Testing\\" + username;
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=" + chromeProfilePath);
        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("--remote-debugging-port=9222");  // Ensure WebSocket debugging is enabled
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--ignore-ssl-errors=yes");
        options.addArguments("--ignore-certificate-errors");
        ChromeDriver driver = new ChromeDriver(options);
        try {
            driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
            driver.get("https://web.whatsapp.com/");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[@class = 'x1qlqyl8 x1pd3egz xcgk4ki']")));
            driver.findElement(By.xpath("//div[@class= '_aohi']")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='xdod15v _ao3e selectable-text copyable-text']")));
            String connectedNumber = driver.findElement(By.xpath("//div[@class= 'xdod15v _ao3e selectable-text copyable-text']")).getText();

            updateWhatsappNumber(username, connectedNumber);
            // Implement logic to verify that the page has loaded and to retrieve the connected number
//            String connectedNumber = "1234567890"; // Placeholder for actual logic

            return connectedNumber;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
		} /*
			 * finally { driver.quit(); }
			 */
    }
    private static void updateWhatsappNumber(String username, String whatsappNumber) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnectionManager.getConnection();
            String sql = "UPDATE register SET WhatsappNumber = ? WHERE username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, whatsappNumber);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}

