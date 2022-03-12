package org.example.core.step;

import org.example.factory.SimpleWebDriverBuilder;
import org.example.factory.WebDriverType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OpenLesson extends AbstractStep {
    private static String
            G_TAB_XPATH = "/html/body/div/div/div/section/section/main/div/div[1]/div/div/ul[2]/li[4]",
            OPEN_LESSON_XPATH = "/html/body/div/div/div/section/section/main/div/div[1]/div/div/div[2]/div[4]/div[2]/div[1]/div/div[2]";

    public OpenLesson(WebDriver webDriver) {
        super(webDriver);
    }


    @Override
    public void run() {
        webDriver.findElement(By.xpath(G_TAB_XPATH)).click();
        webDriver.findElement(By.xpath(OPEN_LESSON_XPATH)).click();
    }
}
