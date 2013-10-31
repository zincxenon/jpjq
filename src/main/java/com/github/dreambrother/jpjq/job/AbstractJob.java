package com.github.dreambrother.jpjq.job;

import com.github.dreambrother.jpjq.executor.JobVisitor;

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

    @Override
    public void visit(JobVisitor visitor) {
        visitor.visit(this);
    }
}
