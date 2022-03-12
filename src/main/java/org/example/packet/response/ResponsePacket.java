package org.example.packet.response;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.packet.Packet;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;

@Getter
public class ResponsePacket implements Packet {
    private final Long ID;

    private int statusCode;

    private String statusMsg;

    private Multimap<String,Object> header = HashMultimap.create();

    private MimeType mimeType;

    private byte[] body;

    private Charset charset;

    public ResponsePacket(){
        ID = INC.incrementAndGet();
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public void setMimeType(MimeType mimeType) {
        this.mimeType = mimeType;
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

    public void addHeader(String name, Object value) {
        this.header.put(name, value);
    }

    public void changeStatus(int states){
        onChangeStatus(states);  // 延迟到子类 ，参考 LinkedHashMap 实现
    }

    protected void onChangeStatus(int status){}

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("\n------------------------------[RESPONSE START] ID:%d------------------------------\n",ID));
        stringBuilder.append("statusCode:").append(statusCode).append('\n');
        stringBuilder.append("headers:").append(header).append('\n');
        stringBuilder.append("[body]:").append('\n');
        if (mimeType!=null && (mimeType.getPrimaryType().equals("text")
                || mimeType.getSubType().equals("json")
                || mimeType.getSubType().equals("script"))){
            stringBuilder.append(StringUtils.abbreviate(new String(body,charset),128));
        }else{
            stringBuilder.append(".......");
        }
        stringBuilder.append(String.format("\n------------------------------[RESPONSE END  ] ID:%d------------------------------\n",ID));
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponsePacket)) return false;
        ResponsePacket that = (ResponsePacket) o;
        return Objects.equals(ID, that.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
