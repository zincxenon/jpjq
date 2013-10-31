package com.github.dreambrother.jpjq.job;

import com.github.dreambrother.jpjq.executor.JobVisitor;

public abstract class RetryJob extends AbstractJob {

    public abstract int retriesCount();

    @Override
    public void visit(JobVisitor visitor) {
        visitor.visit(this);
    }
}
