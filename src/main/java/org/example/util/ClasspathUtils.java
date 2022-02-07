package org.example.util;

import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ClasspathUtils {
    public static String getString(String name) throws IOException {
        return CharStreams.toString(getReader(name));
    }

    public static Reader getReader(String name){
        return new InputStreamReader(getInputStream(name));
    }

    public static InputStream getInputStream(String name){
        return new Object(){}.getClass().getClassLoader().getResourceAsStream(name);
    }
}
