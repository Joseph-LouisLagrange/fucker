package org.example;

import org.example.application.HealthyPunchApp;
import org.junit.Test;

import java.io.IOException;
import java.util.*;



public class AppTest {
    @Test
    public void testOneUserPunch() throws InterruptedException {
        HealthyPunchApp healthyPunchApp = new HealthyPunchApp();
        healthyPunchApp.punch(new User("11815990814","zhou109127...","周小琴"));
    }
}


