package com.scriza.whatappapiBackend;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class SendImage {

    public static void sendImage(String username, String recipient, String filePath) throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:/selenium WebDriver/chromedriver-win64/chromedriver.exe");

        String chromeProfilePath = "E:\\Chrome Profile Testing\\" + username; // Adjust path accordingly
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=" + chromeProfilePath);
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--ignore-ssl-errors=yes");
        options.addArguments("--ignore-certificate-errors");
        ChromeDriver driver = new ChromeDriver(options);
        try {
            driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
            driver.get("https://web.whatsapp.com/");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // Wait for WhatsApp web to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='_ak0w']")));
            // Click on search bar and enter recipient number
            WebElement contactElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@data-icon='new-chat-outline']")));
            contactElement.click();
            WebElement inputBox = driver.switchTo().activeElement();
            inputBox.sendKeys(recipient);
            inputBox.sendKeys(Keys.TAB);Thread.sleep(3000);
            
            
            inputBox.sendKeys(Keys.ENTER);

            // Click on attachment button
            WebElement attachmentButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@data-icon='attach-menu-plus']")));
            attachmentButton.click();
            // Navigate to file upload option using keyboard navigation
            
            WebElement dropDownMenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),\"Photos & videos\")]")));
            dropDownMenu.click();
            
            Robot robot = new Robot();
            robot.delay(3000);
             // Set clipboard with file path
            setClipboardData(filePath);

            // Paste the file path into the file dialog
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.delay(1000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            // Wait for the image to load and send button to be clickable
            Thread.sleep(5000);  // Adjust this wait time as needed

            // Click on send button
            WebElement sendButton = driver.findElement(By.xpath("//span[@data-icon='send']"));
            sendButton.click();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            // Close the browser
//            driver.quit();
     
                // Close the browser
                 driver.quit();

                // Delete the file
                try {
                    Path path = Paths.get(filePath);
                    Files.deleteIfExists(path);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Failed to delete the file: " + filePath, e);
                }
            }
        }

        // Method to set the clipboard data
        private static void setClipboardData(String string) {
            StringSelection stringSelection = new StringSelection(string);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        }
    }