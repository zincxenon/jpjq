package com.github.dreambrother.jpjq.job;

import com.github.dreambrother.jpjq.executor.JobVisitor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.Instant;

public abstract class AbstractJob implements Job {

    private long id;
    private JobStatus jobStatus = JobStatus.INITIAL;
    private Instant creationInstant;

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

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public Instant getCreationInstant() {
        return creationInstant;
    }

    @Override
    public void setCreationInstant(Instant instant) {
        this.creationInstant = instant;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(13, 7, this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
