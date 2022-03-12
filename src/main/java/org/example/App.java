package org.example;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.example.application.HealthyPunchApp;
import org.example.util.PathUtils;

import java.io.IOException;
import java.util.List;


/**
 * Hello world!
 *
 */
@Slf4j
public class App 
{
    public static void main( String[] args ) throws Exception {
        List<User> users = getUsers();
        // 健康打卡
        HealthyPunchApp healthyPunchApp = new HealthyPunchApp();
        healthyPunchApp.punch(users);
    }

    public static List<User> getUsers() throws IOException {
       return new Gson().fromJson(PathUtils.getReaderFromProjectPath("users.json"),new TypeToken<List<User>>(){}.getType());
    }
}
