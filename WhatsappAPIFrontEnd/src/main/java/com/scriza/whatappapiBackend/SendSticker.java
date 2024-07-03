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
import java.time.Duration;

public class SendSticker {

    public static void sendSticker(String username, String recipient, String filePath) throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:/selenium WebDriver/chromedriver-win64/chromedriver.exe");

        String chromeProfilePath = "E:/Chrome Profile Testing/" + username; // Adjust path accordingly
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
            inputBox.sendKeys(Keys.TAB);
            Thread.sleep(1000);

            inputBox.sendKeys(Keys.ENTER);

            // Click on attachment button
            WebElement attachmentButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@data-icon='attach-menu-plus']")));
            attachmentButton.click();
            // Navigate to file upload option using keyboard navigation
            WebElement dropDownMenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'New sticker')]")));
            dropDownMenu.click();

            Robot robot = new Robot();
            robot.delay(1000);
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
            Thread.sleep(2000); // Adjust this wait time as needed

            // Click on send button
            WebElement send = driver.switchTo().activeElement();
            send.sendKeys(Keys.ENTER);
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            // Close the browser
            driver.quit();
        }
    }

    // Method to set the clipboard data
    private static void setClipboardData(String string) {
        StringSelection stringSelection = new StringSelection(string);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }
}
