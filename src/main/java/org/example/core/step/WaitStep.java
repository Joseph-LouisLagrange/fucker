package org.example.core.step;

import java.util.concurrent.TimeUnit;

public class WaitStep implements Step {
    private long time;
    private TimeUnit timeUnit;
    public WaitStep(long time, TimeUnit timeUnit){
        this.time = time;
        this.timeUnit = timeUnit;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(timeUnit.toMillis(time));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
