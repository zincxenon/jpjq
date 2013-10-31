package com.github.dreambrother.jpjq.executor;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.RetryJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobVisitorImpl implements JobVisitor {

    private static final Logger logger = LoggerFactory.getLogger(JobVisitorImpl.class);

    @Override
    public void visit(Job job) {
        job.execute();
    }

    @Override
    public void visit(RetryJob retryJob) {
        for (int i = 1; i < retryJob.retriesCount(); i++) {
            try {
                retryJob.execute();
                return;
            } catch (Exception ex) {
                logger.warn("Attempt {} for job {} with retries count {}", i, retryJob, retryJob.retriesCount());
            }
        }
        // last try
        retryJob.execute();
    }
}
