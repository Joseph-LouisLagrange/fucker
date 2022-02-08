package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.example.util.EventUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@Slf4j
public class CommonAction {

    public static final String LOGIN_URL = "https://uis.cqut.edu.cn/unified_identity_logon/#/login";

    public static final String USERNAME_INPUT_XPATH = "//*[@id=\"app\"]/div/div[1]/div[2]/div/div[2]/form/div[1]/div/div[1]/input";

    public static final String PASSWORD_INPUT_XPATH = "//*[@id=\"app\"]/div/div[1]/div[2]/div/div[2]/form/div[2]/div/div/input";

    public static final String LOGIN_BUTTON_XPATH = "//*[@id=\"app\"]/div/div[1]/div[2]/div/div[2]/button";


    public static void login(WebDriver driver,String username,String password){
        driver.navigate().refresh();
        driver.get(LOGIN_URL);
        driver.manage().window().maximize();
        String currentUrl = driver.getCurrentUrl();
        driver.findElement(By.xpath(USERNAME_INPUT_XPATH)).sendKeys(username);
        driver.findElement(By.xpath(PASSWORD_INPUT_XPATH)).sendKeys(password);
        driver.findElement(By.xpath(LOGIN_BUTTON_XPATH)).click();
    }

}
