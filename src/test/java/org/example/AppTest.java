package org.example;

import static org.junit.Assert.assertTrue;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import net.lightbody.bmp.util.ClasspathResourceUtil;
import org.example.core.AssemblyLiner;
import org.example.core.AssemblyLinerBuilder;
import org.example.core.DefaultAssemblyLinerBuilder;
import org.example.util.ClasspathUtils;
import org.junit.Test;
import org.seleniumhq.jetty9.util.thread.Scheduler;
import org.seleniumhq.jetty9.util.thread.TimerScheduler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;



public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws Exception {
        TimerScheduler scheduler = new TimerScheduler();
        scheduler.start();
        scheduler.schedule(() -> {
            System.out.println(LocalDateTime.now());
        }, 2, TimeUnit.SECONDS);

    }
}


