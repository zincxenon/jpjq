package com.github.dreambrother.jpjq.executor;

import com.github.dreambrother.jpjq.exceptions.IllegalJobStatusException;
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
        checkStatus(job);
        try {
            jobStorage.store(job);

            job.setJobStatus(JobStatus.IN_PROGRESS);
            job.visit(jobVisitor);

            job.setJobStatus(JobStatus.DONE);
        } catch (Exception ex) {
            logger.warn("Job failed {}", job, ex);
            job.setJobStatus(JobStatus.FAILED);
        }
    }

    private void checkStatus(Job job) {
        JobStatus jobStatus = job.getJobStatus();
        if (jobStatus != null && jobStatus != JobStatus.INITIAL) {
            throw new IllegalJobStatusException("Illegal job status " + jobStatus + ". Must be empty or INITIAL");
        }
    }

    public void setJobVisitor(JobVisitor jobVisitor) {
        this.jobVisitor = jobVisitor;
    }

    public void setJobStorage(JobStorage jobStorage) {
        this.jobStorage = jobStorage;
    }
}
