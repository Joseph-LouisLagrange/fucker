package org.example.packet.request;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.io.IOUtils;
import org.example.packet.Packet;
import org.example.util.HttpUtil;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Getter
public class RequestPacket implements Packet {
    private final Long ID;

    private String method;

    private String URL;

    private Multimap<String,Object> header = HashMultimap.create();

    private MimeType mimeType;

    private byte[] body;

    private Charset charset;

    public RequestPacket(){
        ID = INC.incrementAndGet();
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void addHeader(String name,String value) {
        this.header.put(name,value);
    }

    public void setBody(byte[] body) {
        this.body = Arrays.copyOf(body,body.length);
    }

    public void setBody(InputStream inputStream){
        try {
            this.body = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    @Override
    public void setMimeType(MimeType mimeType) {
        this.mimeType = mimeType;
    }

    public Map<String,Object> getQueryParameter(){
        return HttpUtil.getQueryParameter(URL);
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("\n------------------------------[REQUEST START] ID:%d------------------------------\n",ID));
        stringBuilder.append("method:").append(method).append('\n');
        stringBuilder.append("URL:").append(URL).append('\n');
        stringBuilder.append("headers:").append(header).append('\n');
        stringBuilder.append("[body]:").append('\n');
        if (mimeType!=null && (mimeType.getPrimaryType().equals("text")
                || mimeType.getSubType().equals("json")
                || mimeType.getSubType().equals("script"))){
            stringBuilder.append(new String(body,charset));
        }else{
            stringBuilder.append(".......");
        }
        stringBuilder.append(String.format("\n------------------------------[REQUEST END  ] ID:%d------------------------------\n",ID));
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestPacket)) return false;
        RequestPacket that = (RequestPacket) o;
        return ID.equals(that.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
