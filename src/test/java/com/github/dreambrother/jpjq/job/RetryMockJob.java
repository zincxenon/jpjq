package com.github.dreambrother.jpjq.job;

public class RetryMockJob extends RetryJob {

    private Runnable runnableMock;

    public RetryMockJob(int attemptCount, Runnable runnableMock) {
        super(attemptCount);
        this.runnableMock = runnableMock;
    }

    @Override
    public void execute() {
        runnableMock.run();
    }
}
