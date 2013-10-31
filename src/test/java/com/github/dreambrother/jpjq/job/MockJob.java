package com.github.dreambrother.jpjq.job;

public class MockJob extends AbstractJob {

    private Runnable mock;

    public MockJob(Runnable mock) {
        this.mock = mock;
    }

    @Override
    public void execute() {
        mock.run();
    }
}
