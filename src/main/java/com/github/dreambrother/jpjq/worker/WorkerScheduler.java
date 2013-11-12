package com.github.dreambrother.jpjq.worker;

import org.joda.time.Duration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WorkerScheduler {

    private ScheduledExecutorService scheduledExecutorService;

    public void scheduleJobsGcWorker(JobsGcWorker worker, Duration delay) {
        scheduledExecutorService.scheduleAtFixedRate(
                worker,
                delay.getMillis(),
                delay.getMillis(),
                TimeUnit.MILLISECONDS
        );
    }

    public void setScheduledExecutorService(ScheduledExecutorService executorService) {
        this.scheduledExecutorService = executorService;
    }
}
