package org.example.listen;

import org.example.listen.Listener;
import org.example.packet.Packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Spreader<T extends Packet> {
    private BlockingQueue<T> bus = new LinkedBlockingDeque<>();

    private List<Listener<T>> listeners = Collections.synchronizedList(new ArrayList<>());

    public void addListener(Listener<T> listener){
        listeners.add(listener);
    }

    public void spread(T packet){
        listeners.parallelStream()
                .forEach(packetListener -> packetListener.accept(packet));
    }
}
