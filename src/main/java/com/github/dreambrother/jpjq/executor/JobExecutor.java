package com.github.dreambrother.jpjq.executor;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobExecutor {

    private static final Logger logger = LoggerFactory.getLogger(JobExecutor.class);

    private JobVisitor jobVisitor;

    public void execute(Job job) {
        try {
            job.setJobStatus(JobStatus.IN_PROGRESS);
            job.visit(jobVisitor);
            job.setJobStatus(JobStatus.DONE);
        } catch (Exception ex) {
            logger.warn("Job failed {}", job, ex);
            job.setJobStatus(JobStatus.FAILED);
        }
    }

    public void setJobVisitor(JobVisitor jobVisitor) {
        this.jobVisitor = jobVisitor;
    }
}
