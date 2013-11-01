package com.github.dreambrother.jpjq.job;

import com.github.dreambrother.jpjq.visitor.JobVisitor;
import org.joda.time.Instant;

public interface Job {

    long getId();
    void setId(long id);
    Instant getCreationInstant();
    void setCreationInstant(Instant instant);
    void execute();
    JobStatus getJobStatus();
    void setJobStatus(JobStatus jobStatus);
    void visit(JobVisitor visitor);
}
