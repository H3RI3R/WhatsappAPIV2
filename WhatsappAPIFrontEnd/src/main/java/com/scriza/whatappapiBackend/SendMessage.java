package com.scriza.whatappapiBackend;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SendMessage {

    public static void sendMessage(String username, String recipient, String message) throws Exception {
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
//            WebElement searchBar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@data-icon='chats-filled']")));
//            searchBar.click();
            
            // Switch to active element (input box) and send recipient number

            // Wait for contact to appear and click on it
            WebElement contactElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@data-icon='new-chat-outline']")));
            contactElement.click();
            WebElement inputBox = driver.switchTo().activeElement();
            inputBox.sendKeys(recipient);
            inputBox.sendKeys(Keys.TAB);Thread.sleep(3000);
            
            
            inputBox.sendKeys(Keys.ENTER);
//            String contactXPath = "//span[contains(text(), '+91 " + recipient + "')]";
            // Switch to message input box and send message
//            WebElement seeContact = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(contactXPath)));
//            seeContact.click();
            Thread.sleep(1000);
            
            WebElement messageInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label='Type a message']")));
            messageInput.click();
            messageInput.sendKeys(message);

            // Click on send button
            WebElement sendButton = driver.findElement(By.xpath("//button[@aria-label='Send']"));
            sendButton.click();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            // Close the browser
//            driver.quit();
        }
    }
}
