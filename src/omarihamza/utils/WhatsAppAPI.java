package omarihamza.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
        while (true) {
            try {
                mDriver = new ChromeDriver();
                mDriver.get("https://web.whatsapp.com/");
                mDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                WebDriverWait wait = new WebDriverWait(mDriver, 60);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("side")));
                break;
            } catch (Exception e) {
                mDriver.close();
            }
        }

    }

    public static WhatsAppAPI getInstance() {
        if (whatsAppAPI == null) {
            whatsAppAPI = new WhatsAppAPI();
        }
        return whatsAppAPI;
    }

    public void sendMessages(HashMap<String, String> messages) {
        if (!isBrowserOpen) openBrowser();
        ArrayList<String> invalidContacts = new ArrayList<>();
        closeAlertDialogIfExists();
        boolean shouldContinue = false;
        for (HashMap.Entry<String, String> entry : messages.entrySet()) {
            System.out.println("I'm in " + entry.getKey());
            String number = entry.getKey();
            String message = entry.getValue();
            mDriver.get("https://web.whatsapp.com/send?phone=" + number);
            while (true) {
                try {
                    mDriver.get("https://web.whatsapp.com/send?phone=" + number);
                    WebDriverWait wait = new WebDriverWait(mDriver, 60);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main\"]/footer/div[1]/div[2]/div/div[2]")));
                    break;
                } catch (Exception e) {
                    try {
                        WebDriverWait wait3 = new WebDriverWait(mDriver, 1);
                        wait3.until(ExpectedConditions.alertIsPresent());
                        mDriver.switchTo().alert().dismiss();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    } catch (Exception ee) {
                        invalidContacts.add(entry.getKey());
                        shouldContinue = true;
                        break;
                    }
                }
            }
            if (shouldContinue) {
                shouldContinue = false;
                continue;
            }
            WebElement inputBox = mDriver.findElementByXPath("//*[@id=\"main\"]/footer/div[1]/div[2]/div/div[2]");
            //noinspection all
            for (int i = 0; i < message.length(); i++) {
                if (message.charAt(i) == '\n') {
                    Actions actions = new Actions(mDriver);
                    actions.sendKeys(inputBox, Keys.SHIFT, Keys.ENTER).perform();
                } else {
                    try {
                        inputBox.sendKeys(message.charAt(i) + "");
                    } catch (Exception e) {
                        inputBox = mDriver.findElementByXPath("//*[@id=\"main\"]/footer/div[1]/div[2]/div/div[2]");
                        inputBox.sendKeys(message.charAt(i) + "");
                    }
                }
            }
            inputBox.sendKeys(Keys.ENTER);
        }

        if (!invalidContacts.isEmpty()) {
            File file = new File(System.getProperty("user.home") + "/Desktop/invalid_whatsapp_contacts.txt");
            try {
                file.createNewFile();
                StringBuilder contacts = new StringBuilder();
                for (String s : invalidContacts) {
                    contacts.append(s);
                    contacts.append(System.getProperty("line.separator"));
                }
                PrintWriter writer = new PrintWriter(file);
                writer.write(contacts.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                Utils.showPopup("Success", "WhatsApp message sent to " + (messages.size() - invalidContacts.size()) + " out of " + messages.size() + " member\nLog file created under your Desktop directory.", Alert.AlertType.INFORMATION);
            });
        } else
            Platform.runLater(() -> {
                Utils.showPopup("Success", "Successfully sent WhatsApp message to all members", Alert.AlertType.INFORMATION);
            });
    }

    private void closeAlertDialogIfExists() {
        while (true) {
            try {
                WebDriverWait wait3 = new WebDriverWait(mDriver, 1);
                wait3.until(ExpectedConditions.alertIsPresent());
                mDriver.switchTo().alert().dismiss();
                System.out.println("ALERT-");
            } catch (Exception e) {
                System.out.println("NO ALERT-");
                break;
            }
        }
    }
}
