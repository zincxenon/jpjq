package com.github.dreambrother.jpjq.executor;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.RetryWithDelayJob;

public interface JobVisitor {

    void visit(Job job);
    void visit(RetryWithDelayJob job);
}
