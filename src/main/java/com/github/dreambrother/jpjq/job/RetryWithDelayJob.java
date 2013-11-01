package com.github.dreambrother.jpjq.job;

import com.github.dreambrother.jpjq.visitor.JobVisitor;

public abstract class RetryWithDelayJob extends AbstractJob {

    private long delay;
    private int attemptCount;

    public RetryWithDelayJob(long delay, int attemptCount) {
        this.delay = delay;
        this.attemptCount = attemptCount;
    }

    @Override
    public void visit(JobVisitor visitor) {
        visitor.visit(this);
    }

    public long getDelay() {
        return delay;
    }

    public int getAttemptCount() {
        return attemptCount;
    }
}
