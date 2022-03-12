package org.example.util;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;
import org.example.packet.request.BMPRequestPacket;
import org.example.packet.request.NetWorkRequestPacket;
import org.example.packet.request.RequestPacket;
import org.example.packet.response.BMPResponsePacket;
import org.example.packet.response.NetWorkResponsePacket;
import org.example.packet.response.ResponsePacket;

import javax.activation.MimeTypeParseException;

public class PacketUtil {
    public static RequestPacket warp(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) throws MimeTypeParseException {
        BMPRequestPacket packet = new BMPRequestPacket();
        packet.setCharset(contents.getCharset());
        packet.setMimeType(HttpUtil.parseMimeType(contents.getContentType()));
        packet.setURL(messageInfo.getOriginalUrl());
        packet.setMethod(request.method().name());
        packet.setBody(contents.getBinaryContents());
        request.headers().forEach(header->packet.addHeader(header.getKey(),header.getValue()));
        return packet;
    }

    public static ResponsePacket warp(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) throws MimeTypeParseException {
        BMPResponsePacket packet = new BMPResponsePacket(response);
        packet.setStatusCode(response.status().code());
        packet.setStatusMsg(response.status().reasonPhrase());
        response.headers().forEach(header-> packet.addHeader(header.getKey(),header.getValue()));
        packet.setMimeType(HttpUtil.parseMimeType(contents.getContentType()));
        packet.setCharset(contents.getCharset());
        packet.setBody(contents.getBinaryContents());
        return packet;
    }

    public static ResponsePacket warp(org.openqa.selenium.remote.http.HttpResponse httpResponse) throws MimeTypeParseException {
        NetWorkResponsePacket packet = new NetWorkResponsePacket();
        packet.setStatusCode(httpResponse.getStatus());
        String contentType = httpResponse.getHeader(HttpHeaderNames.CONTENT_TYPE.toString());
        if (contentType!=null)
            packet.setMimeType(HttpUtil.parseMimeType(contentType));
        packet.setCharset(httpResponse.getContentEncoding());
        packet.setBody(httpResponse.getContent().get());
        return packet;
    }

    public static RequestPacket warp(org.openqa.selenium.remote.http.HttpRequest httpRequest) throws MimeTypeParseException {
        NetWorkRequestPacket packet = new NetWorkRequestPacket();
        String contentType = httpRequest.getHeader(HttpHeaderNames.CONTENT_TYPE.toString());
        httpRequest.getHeaderNames().forEach(name-> packet.addHeader(name,httpRequest.getHeader(name)));
        packet.setCharset(httpRequest.getContentEncoding());
        if (contentType!=null)
            packet.setMimeType(HttpUtil.parseMimeType(contentType));
        packet.setBody(httpRequest.getContent().get());
        packet.setMethod(httpRequest.getMethod().name());
        packet.setURL(httpRequest.getUri());
        return packet;
    }
}
