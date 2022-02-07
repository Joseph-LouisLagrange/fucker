package org.example.application;

import lombok.extern.slf4j.Slf4j;
import org.example.User;
import org.example.util.StaticFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * 健康管理助手 每日一顶
 */
@Slf4j
public class HealthyPunch {

    public static final String PUNCH_FINISH_KEYWORDS = "今日已顶";

    WebDriver driver;

    public static final String HEALTHY_INDEX_URL = "https://yqgl.cqut.edu.cn/healthdatacollect/defauth/auth/YUN_HUA/100005/mobileHealthIndex";

    public static final String PUNCH_BUTTON_XPATH = "/html/body/div/div/div[2]/button";

    public HealthyPunch(WebDriver driver){
        this.driver = driver;
    }

    public void punch(User user, String stealTicket){
        String fullURL = String.format("%s?ticket=%s", HEALTHY_INDEX_URL, stealTicket);
        driver.get(fullURL);
        driver.manage().window().maximize();
        WebElement punchButton = driver.findElement(By.xpath(PUNCH_BUTTON_XPATH));
        punchButton.click();
        if (punchButton.getText().equals(PUNCH_FINISH_KEYWORDS))
            log.info("{} {}已自动打卡",user.getUsername(),user.getName());
    }

}
