package org.example.packet;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import org.example.packet.request.RequestPacket;
import org.example.packet.response.ResponsePacket;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PacketMemoryImpl implements PacketMemory {
    private Map<Long,RequestPacket> requestPacketCache = new ConcurrentHashMap<>();

    private Map<Long,ResponsePacket> responsePacketCache = new ConcurrentHashMap<>();

    private BiMap<Long,Long> biMap = Maps.synchronizedBiMap(HashBiMap.create());
    @Override
    public void save(RequestPacket requestPacket) {
        requestPacketCache.put(requestPacket.getID(),requestPacket);
    }

    @Override
    public void save(ResponsePacket responsePacket) {
        responsePacketCache.put(responsePacket.getID(), responsePacket);
    }

    @Override
    public void bind(Long requestPID, Long responsePID) {
        biMap.put(requestPID,responsePID);
    }

    @Override
    public Optional<ResponsePacket> lookupResponsePacket(Long PID) {
        return Optional.ofNullable(responsePacketCache.get(biMap.get(PID)));
    }

    @Override
    public Optional<RequestPacket> lookupRequestPacket(Long PID) {
        Long reqID = biMap.inverse().get(PID);
        while (reqID == null){
            reqID = biMap.inverse().get(PID);
        }
        return Optional.ofNullable(requestPacketCache.get(reqID));
    }

    @Override
    public void unbindAndClear(Long requestPID, Long responsePID) {
        biMap.remove(requestPID, responsePID);
        requestPacketCache.remove(requestPID);
        responsePacketCache.remove(responsePID);
    }

    @Override
    public RequestPacket getRequestPacket(Long PID) {
        return requestPacketCache.get(PID);
    }
}
