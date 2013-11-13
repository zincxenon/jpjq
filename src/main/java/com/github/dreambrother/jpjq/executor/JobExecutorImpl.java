package com.github.dreambrother.jpjq.executor;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.JobStatus;
import com.github.dreambrother.jpjq.storage.JobStorage;
import com.github.dreambrother.jpjq.visitor.JobVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobExecutorImpl implements JobExecutor {

    private static final Logger logger = LoggerFactory.getLogger(JobExecutorImpl.class);

    private JobVisitor jobVisitor;
    private JobStorage jobStorage;

    @Override
    public void execute(Job job) {
        try {
            logger.debug("Start executing job {}", job);
            jobStorage.moveToInProgress(job);
            job.setJobStatus(JobStatus.IN_PROGRESS);
            job.visit(jobVisitor);

            logger.debug("Finish executing job {}", job);
            jobStorage.moveToDone(job);
            job.setJobStatus(JobStatus.DONE);
        } catch (Exception ex) {
            logger.warn("Job failed {}", job, ex);

            jobStorage.moveToFailed(job);
            job.setJobStatus(JobStatus.FAILED);
        }
    }

    public void setJobVisitor(JobVisitor jobVisitor) {
        this.jobVisitor = jobVisitor;
    }

    public void setJobStorage(JobStorage jobStorage) {
        this.jobStorage = jobStorage;
    }
}
