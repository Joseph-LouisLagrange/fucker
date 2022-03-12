package org.example.packet.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;
import org.example.packet.request.RequestPacket;

import java.nio.charset.Charset;

public class BMPResponsePacket extends ResponsePacket {
    private HttpResponse response = null;
    public BMPResponsePacket(HttpResponse response){
        this.response = response;
    }

    @Override
    protected void onChangeStatus(int status) {
        response.setStatus(HttpResponseStatus.valueOf(status));
    }

}
