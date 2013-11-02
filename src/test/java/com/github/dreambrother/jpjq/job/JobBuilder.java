package com.github.dreambrother.jpjq.job;

import org.joda.time.Instant;

public class JobBuilder {

    private JobBuilder() {}

    public static SimpleJob initialSimpleJob(String id) {
        return simpleJob(id, JobStatus.INITIAL);
    }

    public static SimpleJob inProgressJob(String id) {
        return simpleJob(id, JobStatus.IN_PROGRESS);
    }
    
    public static SimpleJob doneJob(String id) {
        return simpleJob(id, JobStatus.DONE);
    }

    private static SimpleJob simpleJob(String id, JobStatus status) {
        SimpleJob job = new SimpleJob();
        job.setId(id);
        job.setJobStatus(status);
        job.setCreationInstant(Instant.now());
        return job;
    }
}
