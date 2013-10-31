package com.github.dreambrother.jpjq.job;

import com.github.dreambrother.jpjq.executor.JobVisitor;

public interface Job {

    void execute();
    JobStatus getJobStatus();
    void setJobStatus(JobStatus jobStatus);
    void visit(JobVisitor visitor);
}
