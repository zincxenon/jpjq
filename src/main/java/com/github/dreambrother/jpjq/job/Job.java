package com.github.dreambrother.jpjq.job;

public interface Job {

    void execute();
    JobStatus getJobStatus();
    void setJobStatus(JobStatus jobStatus);
}
