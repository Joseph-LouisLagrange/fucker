package org.example.steal;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.example.packet.PacketMemory;
import org.example.packet.request.RequestPacket;
import org.example.util.EventUtil;
import org.example.packet.response.ResponsePacket;
import org.example.util.JsonUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.Optional;


/**
 * 从 [个人课表] 偷取 ticket
 */
@Slf4j
public class PersonLessonTicketStealer implements ApplicationTicketStealer {

    private PacketMemory packetMemory;

    private ApplicationTicketReceiver applicationTicketReceiver;

    public PersonLessonTicketStealer(PacketMemory packetMemory,ApplicationTicketReceiver applicationTicketReceiver){
        this.packetMemory = packetMemory;
        this.applicationTicketReceiver = applicationTicketReceiver;
    }

    @Override
    public void accept(ResponsePacket packet) {
        Optional<RequestPacket> optional = packetMemory.lookupRequestPacket(packet.getID());
        while (!optional.isPresent()){
            optional = packetMemory.lookupRequestPacket(packet.getID());
        }
        RequestPacket requestPacket = optional.get();
        String url = requestPacket.getURL();
        if (url.contains("getApplicationUrl") && url.contains("applicationCode")){
            Map<String,Object> map = JsonUtil.JSON2Map(packet.getBody(), packet.getCharset());
            String stealTicket = (String) ((Map) map.get("content")).get("ticket");
            log.info("stealTicket : {}",stealTicket);
            try {
                applicationTicketReceiver.receive(stealTicket);
            }finally {
                packet.changeStatus(HttpResponseStatus.BAD_REQUEST.code());
            }
        }
    }
}
