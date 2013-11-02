package com.github.dreambrother.jpjq.job;

import org.joda.time.Instant;

import java.util.UUID;

public class JobBuilder {

    private JobBuilder() {}

    public static SimpleJob initialSimpleJob() {
        SimpleJob job = new SimpleJob();
        job.setId(UUID.randomUUID().toString());
        job.setJobStatus(JobStatus.INITIAL);
        job.setCreationInstant(Instant.now());
        return job;
    }
}
