package com.github.dreambrother.jpjq.visitor;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.RetryWithDelayJob;
import com.github.dreambrother.jpjq.service.DelayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutorJobVisitor implements JobVisitor {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorJobVisitor.class);

    private DelayService delayService;

    @Override
    public void visit(Job job) {
        job.execute();
    }

    @Override
    public void visit(RetryWithDelayJob job) {
        for (int i = 1; i < job.getAttemptCount(); i++) {
            try {
                job.execute();
                return;
            } catch (Exception ex) {
                logger.warn("Attempt {} for job {} with attempts count {}", i, job, job.getAttemptCount());
                if (job.getDelay() > 0) {
                    logger.info("Wait {} millis before next attempt");
                    delayService.delay(job.getDelay());
                }
            }
        }
        // last try
        job.execute();
    }

    public void setDelayService(DelayService delayService) {
        this.delayService = delayService;
    }
}
