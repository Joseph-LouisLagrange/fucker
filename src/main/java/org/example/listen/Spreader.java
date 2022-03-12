package org.example.listen;

import com.google.common.collect.Lists;
import org.example.listen.Listener;
import org.example.packet.Packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Spreader<T extends Packet> {

    private List<Listener<T>> listeners;

    private Context context;

    @SafeVarargs
    public Spreader(Listener<T>...listeners){
        this.listeners = Collections.synchronizedList(Lists.newArrayList(listeners));
    }

    public void addListener(Listener<T> listener){
        listeners.add(listener);
    }

    public void spread(T packet){
        listeners.parallelStream()
                .forEach(packetListener -> packetListener.accept(packet));
    }
}
