package org.example.packet;

import com.google.gson.Gson;
import org.example.packet.InterceptedHttpServletResponse;
import org.example.packet.Packet;

import java.util.Map;

public class ResponsePacket implements Packet {

    protected InterceptedHttpServletResponse interceptedHttpServletResponse;


    public InterceptedHttpServletResponse getInterceptedHttpServletResponse() {
        return interceptedHttpServletResponse;
    }

    public void setStatus(int states){
        interceptedHttpServletResponse.setStatus(states);
        onSetStatus(states);  // 延迟到子类 ，参考 LinkedHashMap 实现
    }


    protected void onSetStatus(int status){}

    public Map<String,Object> getJsonMapper(){
        return (Map<String,Object>)new Gson().fromJson(interceptedHttpServletResponse.getTextContent(),Map.class);
    }

}
