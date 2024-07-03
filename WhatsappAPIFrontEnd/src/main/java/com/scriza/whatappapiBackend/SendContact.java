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

public class SendContact {

    public static void sendContact(String username, String recipient, String contactNumber, String contactName) throws Exception {
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
            inputBox.sendKeys(Keys.TAB);
            Thread.sleep(3000);

            inputBox.sendKeys(Keys.ENTER);

            WebElement send = driver.switchTo().activeElement();
            send.sendKeys(contactNumber);
            send.sendKeys(Keys.ENTER);
            send.sendKeys(contactName);
            send.sendKeys(Keys.ENTER);
            // Enter contact details
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            // Close the browser
//            driver.quit();
        }
    }
}
