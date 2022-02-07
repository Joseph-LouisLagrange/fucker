package org.example.steal;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.example.util.EventUtil;
import org.example.packet.ResponsePacket;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Map;

/**
 * 从 [个人课表] 偷取 ticket
 */
public class PersonLessonTicketProxyStealer implements ApplicationTicketProxyStealer {

    private volatile String stealTicket = null;

    private WebDriver driver;

    public PersonLessonTicketProxyStealer(WebDriver driver){
        this.driver = driver;
    }

    @Override
    public void accept(ResponsePacket packet) {
        String url = packet.getInterceptedHttpServletResponse().getUrl();
        if (url.contains("getApplicationUrl") && url.contains("applicationCode")){
            Map<String,Object> map = packet.getJsonMapper();
            stealTicket = (String) ((Map) map.get("content")).get("ticket");
            packet.setStatus(HttpResponseStatus.BAD_REQUEST.code());
        }
    }

    @Override
    public String steal() {
        EventUtil.click(driver,driver.findElement(By.xpath("/html/body/div/div/div/section/aside/div[2]/div[1]/div/ul/li[3]")));
        driver.findElement(By.xpath("/html/body/div/div/div/section/section/main/div/div[1]/div/div/ul[2]/li[4]")).click();
        driver.findElement(By.xpath("/html/body/div/div/div/section/section/main/div/div[1]/div/div/div[2]/div[4]/div[2]/div[1]/div/div[2]")).click();
        // 循环等待 偷取票据
        while (stealTicket==null);
        return stealTicket;
    }
}
