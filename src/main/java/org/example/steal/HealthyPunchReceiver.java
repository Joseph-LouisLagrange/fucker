package org.example.steal;

import lombok.extern.slf4j.Slf4j;
import org.example.User;
import org.example.factory.SimpleWebDriverBuilder;
import org.example.factory.WebDriverType;
import org.example.util.EventUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

@Slf4j
public class HealthyPunchReceiver implements ApplicationTicketReceiver {
    public static final String HEALTHY_INDEX_URL = "https://yqgl.cqut.edu.cn/healthdatacollect/defauth/auth/YUN_HUA/100005/mobileHealthIndex";

    private static final String PUNCH_BUTTON_XPATH = "/html/body/div/div/div[2]/button";

    private User user;


    public HealthyPunchReceiver(){}


    public HealthyPunchReceiver(User user) {
        this.user = user;
    }

    @Override
    public void receive(String ticket) {
        WebDriver webDriver = new SimpleWebDriverBuilder().build(WebDriverType.FIREFOX);
        try{
            String fullURL = String.format("%s?ticket=%s", HEALTHY_INDEX_URL, ticket);
            webDriver.navigate().to(fullURL);
            WebElement button = webDriver.findElement(By.xpath(PUNCH_BUTTON_XPATH));
            EventUtil.click(webDriver,button);
            EventUtil.click(webDriver,button);
            webDriver.navigate().refresh();
            if (webDriver.findElement(By.xpath(PUNCH_BUTTON_XPATH)).getText().equals("今日已顶")) {
                log.info("{}-{} 打卡成功", user.getUsername(), user.getName());
            }
        }finally {
            webDriver.quit();
        }

    }

    public void setUser(User user) {
        this.user = user;
    }
}
