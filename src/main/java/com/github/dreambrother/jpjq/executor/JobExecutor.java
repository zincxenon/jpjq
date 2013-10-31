package com.github.dreambrother.jpjq.executor;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.JobStatus;

public class JobExecutor {

    public void execute(Job job) {
        try {
            job.execute();
            job.setJobStatus(JobStatus.DONE);
        } catch (Exception ex) {
            job.setJobStatus(JobStatus.FAILED);
        }
    }
}
