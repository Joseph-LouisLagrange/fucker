package org.example.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.commons.compress.utils.ByteUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.Map;

public class JsonUtil {
    static Gson gson = new Gson();
    public static Map<String,Object> JSON2Map(String json){
       return gson.fromJson(json,new TypeToken<Map<String,Object>>(){}.getType());
    }

    public static Map<String,Object> JSON2Map(byte[] json, Charset charset){
        return JSON2Map(new String(json,charset));
    }

}
