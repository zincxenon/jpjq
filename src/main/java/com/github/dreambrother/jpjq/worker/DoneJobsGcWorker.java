package com.github.dreambrother.jpjq.worker;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.storage.JobStorage;
import org.joda.time.Duration;
import org.joda.time.Instant;

import java.util.List;

public class DoneJobsGcWorker implements Runnable {

    private Duration expirationDuration;
    private JobStorage jobStorage;

    @Override
    public void run() {
        List<? extends Job> jobs = jobStorage.findDone();
        long currentTimeMillis = Instant.now().getMillis();
        for (Job job : jobs) {
            if (currentTimeMillis - job.getCreationInstant().getMillis() >= expirationDuration.getMillis()) {
                jobStorage.remove(job);
            }
        }
    }

    public void setExpirationDuration(Duration expirationDuration) {
        this.expirationDuration = expirationDuration;
    }

    public void setJobStorage(JobStorage jobStorage) {
        this.jobStorage = jobStorage;
    }
}
