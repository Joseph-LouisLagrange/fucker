package org.example.core.step;

import org.example.util.EventUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EnterServiceCenter extends AbstractStep {
    private static String SERVICE_CENTER_XPATH = "//*[@id=\"app\"]/div/div/section/aside/div[2]/div[1]/div/ul/li[3]";

    public EnterServiceCenter(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void run() {
        EventUtil.click(webDriver,webDriver.findElement(By.xpath(SERVICE_CENTER_XPATH)));
    }
}
