package com.github.dreambrother.jpjq.job;

import com.github.dreambrother.jpjq.visitor.JobVisitor;
import org.joda.time.Duration;

public abstract class RetryJob extends RetryWithDelayJob {

    private static final Duration ZERO_DELAY = Duration.ZERO;

    public RetryJob(int attemptCount) {
        super(ZERO_DELAY, attemptCount);
    }

    @Override
    public void visit(JobVisitor visitor) {
        visitor.visit(this);
    }
}
