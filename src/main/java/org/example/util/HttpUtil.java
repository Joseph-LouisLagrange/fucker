package org.example.util;

import io.netty.handler.codec.http.HttpHeaderNames;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    public static Map<String,Object> getQueryParameter(String URL){
        int index = URL.lastIndexOf('?');
        if (index<0){
            return Collections.emptyMap();
        }
        String queryString = URL.substring(index);
        String[] kvs = queryString.split("&");
        Map<String,Object> queryParameter = new HashMap<>();
        for (String kv : kvs) {
            String[] temp = kv.split("=");
            queryParameter.put(temp[0],temp[1]);
        }
        return queryParameter;
    }

    public static MimeType parseMimeType(String contentType) throws MimeTypeParseException {
        return new MimeType(io.netty.handler.codec.http.HttpUtil.getMimeType(contentType).toString());
    }
}
