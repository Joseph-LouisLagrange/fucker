package org.example.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.StringJoiner;

public class WebDriverUtil {
    public static void execJS(WebDriver webDriver, String js){
        if (ClassUtils.isAssignable(webDriver.getClass(),ChromeDriver.class)){
            ((ChromeDriver)webDriver).executeScript(js);
        }
    }

    public static void execJSFunc(WebDriver webDriver,String func,Object...args){
        execJS(webDriver,String.format("%s(%s);",func,StringUtils.join(args,",")));
    }
}
