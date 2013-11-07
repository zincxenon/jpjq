package com.github.dreambrother.jpjq.job;

import org.joda.time.Instant;

import java.util.UUID;

public class JobBuilder {

    private JobBuilder() {}

    public static SimpleJob initialJob() {
        return simpleJob(JobStatus.INITIAL);
    }

    public static SimpleJob inProgressJob() {
        return simpleJob(JobStatus.IN_PROGRESS);
    }
    
    public static SimpleJob doneJob() {
        return simpleJob(JobStatus.DONE);
    }

    public static SimpleJob failedJob() {
        return simpleJob(JobStatus.FAILED);
    }

    public static SimpleJob emptyJob() {
        SimpleJob job = new SimpleJob();
        job.setCreationInstant(null);
        job.setJobStatus(null);
        return job;
    }

    private static SimpleJob simpleJob(JobStatus status) {
        SimpleJob job = new SimpleJob();
        job.setId(UUID.randomUUID().toString());
        job.setJobStatus(status);
        job.setCreationInstant(Instant.now());
        return job;
    }
}
