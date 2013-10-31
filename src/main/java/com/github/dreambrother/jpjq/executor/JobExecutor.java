package com.github.dreambrother.jpjq.executor;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobExecutor {

    private static final Logger logger = LoggerFactory.getLogger(JobExecutor.class);

    public void execute(Job job) {
        try {
            job.execute();
            job.setJobStatus(JobStatus.DONE);
        } catch (Exception ex) {
            logger.info("Job failed {}", job, ex);
            job.setJobStatus(JobStatus.FAILED);
        }
    }
}
