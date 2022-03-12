package org.example.core.step;

import org.example.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class LoginStep extends AbstractStep {

    private static final String USERNAME_INPUT_XPATH = "//*[@id=\"app\"]/div/div[1]/div[2]/div/div[2]/form/div[1]/div/div[1]/input";

    private static final String PASSWORD_INPUT_XPATH = "//*[@id=\"app\"]/div/div[1]/div[2]/div/div[2]/form/div[2]/div/div/input";

    private static final String LOGIN_BUTTON_XPATH = "//*[@id=\"app\"]/div/div[1]/div[2]/div/div[2]/button";

    private User user;


    public LoginStep(WebDriver webDriver, User user) {
        super(webDriver);
        this.user = user;
    }

    @Override
    public void run() {
        webDriver.get("https://uis.cqut.edu.cn/unified_identity_logon/#/login");
        webDriver.findElement(By.xpath(USERNAME_INPUT_XPATH)).sendKeys(user.getUsername());
        webDriver.findElement(By.xpath(PASSWORD_INPUT_XPATH)).sendKeys(user.getPassword());
        webDriver.findElement(By.xpath(LOGIN_BUTTON_XPATH)).click();
    }
}
