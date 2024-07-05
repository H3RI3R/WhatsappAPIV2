package com.scriza.whatappapiBackend;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

public class SendIndividualMessage {

    public static void sendMessages(String username, Map<String, String> phoneNumberMessageMap) {
        System.setProperty("webdriver.chrome.driver", "C:/selenium WebDriver/chromedriver-win64/chromedriver.exe");

        String chromeProfilePath = "E:\\Chrome Profile Testing\\" + username;
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
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='_ak0w']")));

            // Debug: Print the map contents
            System.out.println("Phone Number - Message Map:");
            for (Map.Entry<String, String> entry : phoneNumberMessageMap.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }

            // Send individual messages
            for (Map.Entry<String, String> entry : phoneNumberMessageMap.entrySet()) {
                String phoneNumber = entry.getKey();
                String message = entry.getValue();

                // New chat button
                WebElement newChatButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@data-icon='new-chat-outline']")));
                newChatButton.click();

                // Search for the phone number and send the message
                WebElement searchBox = driver.switchTo().activeElement();
                searchBox.sendKeys(phoneNumber);
                searchBox.sendKeys(Keys.TAB);
                Thread.sleep(1000);  // Exception handled below
                searchBox.sendKeys(Keys.ENTER);
                Thread.sleep(1000);  // Exception handled below

                // Send the message
                WebElement messageBox = driver.switchTo().activeElement();
                messageBox.sendKeys(message);
                messageBox.sendKeys(Keys.ENTER);
            }
        } catch (InterruptedException e) {
            System.err.println("InterruptedException occurred: " + e.getMessage());
            Thread.currentThread().interrupt();  // Restore interrupted status
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while sending messages", e);
        } finally {
//            driver.quit();
        }
    }
}
