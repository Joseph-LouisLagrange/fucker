package org.example.factory.proxy;

import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.proxy.CaptureType;
import org.example.listen.Spreader;
import org.example.packet.PacketMemory;
import org.example.packet.request.RequestPacket;
import org.example.packet.response.ResponsePacket;
import org.example.util.PacketUtil;

import javax.activation.MimeTypeParseException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BrowseMobProxyFactory implements ProxyFactory {
    @Override
    public SeleniumProxy getProxy(Spreader<RequestPacket> requestPacketSpreader,
                                  Spreader<ResponsePacket> responsePacketSpreader,
                                  PacketMemory packetMemory) {
        BrowserMobProxyServer browserMobProxyServer = new BrowserMobProxyServer();
        browserMobProxyServer.setTrustAllServers(true);
        ConcurrentMap<String,Long> tempMap = new ConcurrentHashMap<>();
        browserMobProxyServer.addRequestFilter((httpRequest, httpMessageContents, httpMessageInfo) -> {
            // 包装为 RequestPacket
            RequestPacket requestPacket = null;
            try {
                requestPacket = PacketUtil.warp(httpRequest, httpMessageContents, httpMessageInfo);
            } catch (MimeTypeParseException e) {
                e.printStackTrace();
            }
            // 缓存这个 RequestPacket
            packetMemory.save(requestPacket);
            // 临时存储
            assert requestPacket != null;
            tempMap.put(httpMessageInfo.getOriginalUrl(), requestPacket.getID());
            // 传播给各个Listener
            requestPacketSpreader.spread(requestPacket);
            return null;
        });
        browserMobProxyServer.addResponseFilter((httpResponse, httpMessageContents, httpMessageInfo) -> {
            String URL = httpMessageInfo.getOriginalUrl();
            // 包装为 ResponsePacket
            ResponsePacket responsePacket = null;
            try {
                responsePacket = PacketUtil.warp(httpResponse, httpMessageContents, httpMessageInfo);
            } catch (MimeTypeParseException e) {
                e.printStackTrace();
            }
            // 缓存这个 ResponsePacket
            packetMemory.save(responsePacket);
            assert responsePacket != null;
            Long rspPID = responsePacket.getID();
            Long reqPID = tempMap.remove(URL);
            // 绑定 Request --- Response 的对相应关系
            packetMemory.bind(reqPID,rspPID);
            // 传播给各个Listener
            responsePacketSpreader.spread(responsePacket);
            // 删除 Packet 缓存
            packetMemory.unbindAndClear(reqPID,rspPID);
        });
        browserMobProxyServer.enableHarCaptureTypes(CaptureType.REQUEST_HEADERS,CaptureType.REQUEST_CONTENT,CaptureType.RESPONSE_CONTENT,CaptureType.RESPONSE_HEADERS);
        return new BMSeleniumProxy(browserMobProxyServer);
    }
}
