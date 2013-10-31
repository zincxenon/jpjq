package com.github.dreambrother.jpjq.job;

public abstract class AbstractJob implements Job {

    private JobStatus jobStatus;

    @Override
    public JobStatus getJobStatus() {
        return jobStatus;
    }

    @Override
    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }
}
