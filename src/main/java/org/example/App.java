package org.example;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.util.ClassPath;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import net.lightbody.bmp.util.ClasspathResourceUtil;
import org.apache.commons.lang3.ClassPathUtils;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.example.listen.Spreader;
import org.example.logic.AutomaticFunction;
import org.example.packet.RequestPacket;
import org.example.packet.ResponsePacket;
import org.example.steal.ApplicationTicketStealer;
import org.example.steal.PersonLessonTicketProxyStealer;
import org.example.util.ClasspathUtils;
import org.example.util.CommonAction;
import org.example.util.StaticFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ){
        AutomaticFunction.defaultGoreAtHealthManagement();
    }

}