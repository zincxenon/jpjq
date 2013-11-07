package com.github.dreambrother.jpjq.job;

public class RetryWithDelayMockJob extends RetryWithDelayJob {

    private Runnable runnableMock;

    public RetryWithDelayMockJob(long delay, int attemptCount, Runnable runnableMock) {
        super(delay, attemptCount);
        this.runnableMock = runnableMock;
    }

    @Override
    public void execute() {
        runnableMock.run();
    }
}
