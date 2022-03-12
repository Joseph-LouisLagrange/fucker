package org.example.core.step;

import org.openqa.selenium.WebDriver;

public abstract class AbstractStep implements Step {
    protected WebDriver webDriver;
    public AbstractStep(WebDriver webDriver){
        this.webDriver = webDriver;
    }
    protected WebDriver getWebDriver(){
        return webDriver;
    }
}
