package omarihamza.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class WhatsAppAPI {

    private static WhatsAppAPI whatsAppAPI = null;
    private ChromeDriver mDriver;
    private static final String DRIVER_LOCATION = "C:\\chromedriver.exe";
    private static final String DRIVER_NAME = "webdriver.chrome.driver";
    private static boolean isBrowserOpen = false;

    private WhatsAppAPI() {

    }

    private void openBrowser() {
        isBrowserOpen = true;
        System.setProperty(DRIVER_NAME, DRIVER_LOCATION);
        mDriver = new ChromeDriver();
        mDriver.get("https://web.whatsapp.com/");
        mDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(mDriver, 60);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("side")));
    }

    public static WhatsAppAPI getInstance() {
        if (whatsAppAPI == null) {
            whatsAppAPI = new WhatsAppAPI();
        }
        return whatsAppAPI;
    }

    public void sendMessages(HashMap<String, String> messages) {
        if (!isBrowserOpen) openBrowser();
        for (HashMap.Entry<String, String> entry : messages.entrySet()) {
            String number = entry.getKey();
            String message = entry.getValue();
            ((JavascriptExecutor)mDriver).executeScript("window.open();");
            mDriver.get("https://web.whatsapp.com/send?phone=" + number);
            WebElement inputBox = mDriver.findElementByXPath("//*[@id=\"main\"]/footer/div[1]/div[2]/div/div[2]");
            //noinspection all
            for (int i = 0; i < message.length(); i++) {
                if (message.charAt(i) == '\n') {
                    Actions actions = new Actions(mDriver);
                    actions.sendKeys(inputBox, Keys.SHIFT, Keys.ENTER).perform();
                } else {
                    inputBox.sendKeys(message.charAt(i) + "");
                }
            }
            inputBox.sendKeys(Keys.ENTER);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
