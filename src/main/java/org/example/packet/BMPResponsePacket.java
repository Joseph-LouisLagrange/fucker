package org.example.packet;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

public class BMPResponsePacket extends ResponsePacket {
    private HttpResponse response = null;
    public BMPResponsePacket(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo){
        this.response = response;
        interceptedHttpServletResponse = new InterceptedHttpServletResponseImpl();
        interceptedHttpServletResponse.setUrl(messageInfo.getOriginalUrl());
        interceptedHttpServletResponse.setTextContent(contents.getTextContents());
    }


    @Override
    protected void onSetStatus(int status) {
        response.setStatus(HttpResponseStatus.valueOf(status));
    }
}
