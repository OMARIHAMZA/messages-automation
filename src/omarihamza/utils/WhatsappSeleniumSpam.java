package omarihamza.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.InputSource;
import org.openqa.selenium.interactions.Interaction;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.security.Key;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class WhatsappSeleniumSpam {

    public static final String WHATSAPP_LINK = "https://web.whatsapp.com/";

    //Selenium Driver Location
    public static final String DRIVER_LOCATION = "C:\\chromedriver.exe";
    public static final String DRIVER_NAME = "webdriver.chrome.driver";
    //Display Name Of Contact
    public static final String PERSON_NAME = "Harvard";
    //Spam Message
    public static final String MESSAGE = "Test Message at " + Calendar.getInstance().getTime();
    public static final int SPAM_COUNT = 5;


    public static void main(String[] args) {

        System.setProperty(DRIVER_NAME, DRIVER_LOCATION);
        ChromeDriver driver = new ChromeDriver();

        driver.get("https://web.whatsapp.com/send?phone=" + "+971569520766");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("side")));

        String message = "d";

        WebElement inputBox = driver.findElementByXPath("//*[@id=\"main\"]/footer/div[1]/div[2]/div/div[2]");


        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == '\n') {
                Actions actions = new Actions(driver);
                actions.sendKeys(inputBox, Keys.SHIFT, Keys.ENTER).perform();
            } else {
                inputBox.sendKeys(message.charAt(i) + "");
            }
        }
        inputBox.sendKeys(Keys.ENTER);

    }
}
