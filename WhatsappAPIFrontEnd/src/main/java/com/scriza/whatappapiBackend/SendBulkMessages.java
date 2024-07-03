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
import java.util.List;

public class SendBulkMessages {

    public static void sendMessages(String username, List<String> phoneNumbers , String message) throws Exception {
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

            // Create a new group
            WebElement newChatButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@data-icon='new-chat-outline']")));
            newChatButton.click();
            WebElement newGroupButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'New group')]")));
            newGroupButton.click();

            // Add phone numbers to the group
            for (String phoneNumber : phoneNumbers) {
                WebElement inputBox = driver.switchTo().activeElement();
                inputBox.sendKeys(phoneNumber);
                inputBox.sendKeys(Keys.TAB);
                Thread.sleep(1000);
                inputBox.sendKeys(Keys.ENTER);
                Thread.sleep(1000);
            }

            // Click on the next button to create the group
            WebElement nextButton = driver.findElement(By.xpath("//span[@data-icon='arrow-forward']"));
            nextButton.click();

            // Enter group name and create the group
//            WebElement groupNameBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@contenteditable='true']")));
//            groupNameBox.sendKeys("Bulk Message Group");
            WebElement createGroupButton = driver.findElement(By.xpath("//span[@data-icon='checkmark-medium']"));
            createGroupButton.click();
            Thread.sleep(2000);
            // Wait for group creation and send the message
            WebElement send =   driver.switchTo().activeElement();
            send.sendKeys(message);
            send.sendKeys(Keys.ENTER);
//            sendButton.click();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
//            driver.quit();
        }
    }
}
