package com.scriza.whatappapiBackend;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
public class BankWebSocketServer {
    private static final String SCREENSHOT_PATH = "C:\\Users\\H3RI3R\\git\\WhatsappApI\\WhatsappAPI\\Screenshots\\QRCODE.png";
    private static final String HTML_FILE_PATH = "C:\\Users\\H3RI3R\\git\\WhatsappApI\\WhatsappAPI\\src\\main\\webapp\\index.html";
    private static final String CHROME_PROFILE_BASE_PATH = "E:\\Chrome Profile Testing\\";

    public static void main(String[] args) {
        // Main method implementation, can be kept or modified based on your application structure
    }

    public static void startQrScanning(String username) throws IOException, InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:/selenium WebDriver/chromedriver-win64/chromedriver.exe");
        int count =  0;
        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("user-data-dir=" + CHROME_PROFILE_BASE_PATH + username);

        // Start Chrome WebDriver
        ChromeDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://web.whatsapp.com/"); // Open WhatsApp Web

        WebDriverWait wait = new WebDriverWait(driver, 10);
        File screenshotFile = new File(SCREENSHOT_PATH);
        openHtmlInBrowser();
        while (true) {
            try {
                // Wait for QR code element to be visible
                WebElement qrElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='_akau']")));
                File screenshot = ((TakesScreenshot) qrElement).getScreenshotAs(OutputType.FILE);
                BufferedImage qrImage = ImageIO.read(screenshot);
                ImageIO.write(qrImage, "png", screenshotFile);
                System.out.println("Screenshot taken and saved.");
                QrCodeServerEndpoint.notifyClients(); // Notify clients about the new QR code
            } catch (Exception e) {
                // Handle element not found or other exceptions
                if (isElementPresent(driver, By.xpath("//span[@data-icon='refresh-large']"))) {
                    WebElement refreshElement = driver.findElement(By.xpath("//span[@data-icon='refresh-large']"));
                    refreshElement.click(); // Refresh the page if necessary
                }
                count +=1;
                System.out.println("An error occurred while taking the screenshot: " + e.getMessage());
                if (count ==5) {
                	driver.quit();
                	break;
                }
            }

            // Check for login success
            if (isElementPresent(driver, By.xpath("//div[contains(@class, '_aly_') and contains(text(), 'Loading your chats')]"))) {
                System.out.println("Login successful!");
                QrCodeServerEndpoint.notifyLoginSuccess(); // Notify clients about the login success
                break;
            }

            Thread.sleep(2000); // Wait for 2 seconds before retrying
        }

        // Close the browser after saving the profile
//        driver.quit();
    }

    // Utility method to check if an element is present
    private static boolean isElementPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void openHtmlInBrowser() {
        try {
            File htmlFile = new File(HTML_FILE_PATH);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(htmlFile.toURI());
            } else {
                System.err.println("Desktop is not supported. Cannot open the HTML file in the default browser.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}