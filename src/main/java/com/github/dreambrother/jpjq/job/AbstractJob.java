package com.github.dreambrother.jpjq.job;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.dreambrother.jpjq.visitor.JobVisitor;
import com.github.dreambrother.jpjq.json.InstantSerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Instant;

import java.util.UUID;

/**
 * Not thread safe. Do not share between threads.
 */
public abstract class AbstractJob implements Job {

    private String id = UUID.randomUUID().toString();
    private JobStatus jobStatus = JobStatus.INITIAL;
    @JsonSerialize(using = InstantSerializer.class)
    private Instant creationInstant = Instant.now();

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
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
