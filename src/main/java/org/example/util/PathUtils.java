package org.example.util;

import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class PathUtils {
    public static String getTextFromClassPath(String name) throws IOException {
        return CharStreams.toString(getReaderFromPath(name));
    }

    public static Reader getReaderFromPath(String name){
        return new InputStreamReader(getInputStreamFromPath(name));
    }

    public static InputStream getInputStreamFromPath(String name){
        return new Object(){}.getClass().getClassLoader().getResourceAsStream(name);
    }

    public static InputStream getInputStreamFromProjectPath(String name) throws IOException {
        String filePath = String.format("%s/%s", System.getProperty("user.dir"), name);
        return FileUtils.openInputStream(new File(filePath));
    }

    public static Reader getReaderFromProjectPath(String name) throws IOException{
        return new InputStreamReader(getInputStreamFromProjectPath(name));
    }

    public static String getTextFromProjectPath(String name) throws IOException {
        return CharStreams.toString(getReaderFromProjectPath(name));
    }
}
