package org.example;

import static org.junit.Assert.assertTrue;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import net.lightbody.bmp.util.ClasspathResourceUtil;
import org.apache.commons.lang.time.DateUtils;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;



public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws Exception {
        Date now = new Date();
//        now.setHours(0);
//        now.setMinutes(0);
//        now.setSeconds(0);
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("当前时间为: " + ft.format(now));
    }
}


