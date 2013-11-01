package com.github.dreambrother.jpjq.job;

import com.github.dreambrother.jpjq.executor.JobVisitor;

public abstract class RetryJob extends RetryWithDelayJob {

    private static final long ZERO_DELAY = 0;

    public RetryJob(int attemptCount) {
        super(ZERO_DELAY, attemptCount);
    }

    @Override
    public void visit(JobVisitor visitor) {
        visitor.visit(this);
    }
}
