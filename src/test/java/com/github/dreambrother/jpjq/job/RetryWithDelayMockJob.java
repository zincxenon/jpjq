package com.github.dreambrother.jpjq.job;

import org.joda.time.Duration;

public class RetryWithDelayMockJob extends RetryWithDelayJob {

    private Runnable runnableMock;

    public RetryWithDelayMockJob(long delayMillis, int attemptCount, Runnable runnableMock) {
        super(Duration.millis(delayMillis), attemptCount);
        this.runnableMock = runnableMock;
    }

    @Override
    public void execute() {
        runnableMock.run();
    }
}
