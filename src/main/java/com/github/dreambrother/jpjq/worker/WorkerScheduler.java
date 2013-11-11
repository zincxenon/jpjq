package com.github.dreambrother.jpjq.worker;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WorkerScheduler {

    private ScheduledExecutorService scheduledExecutorService;

    public void scheduleDoneJobsGcWorker(DoneJobsGcWorker worker) {
        scheduledExecutorService.scheduleWithFixedDelay(
                worker,
                worker.getExpirationDuration().getMillis(),
                worker.getExpirationDuration().getMillis(),
                TimeUnit.MILLISECONDS
        );
    }

    public void setScheduledExecutorService(ScheduledExecutorService executorService) {
        this.scheduledExecutorService = executorService;
    }
}
