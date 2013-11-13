package com.github.dreambrother.jpjq.job;

import com.github.dreambrother.jpjq.visitor.JobVisitor;
import org.joda.time.Duration;

public abstract class RetryWithDelayJob extends AbstractJob {

    private Duration delay;
    private int attemptCount;

    public RetryWithDelayJob(Duration delay, int attemptCount) {
        this.delay = delay;
        this.attemptCount = attemptCount;
    }

    @Override
    public void visit(JobVisitor visitor) {
        visitor.visit(this);
    }

    public Duration getDelay() {
        return delay;
    }

    public int getAttemptCount() {
        return attemptCount;
    }
}
