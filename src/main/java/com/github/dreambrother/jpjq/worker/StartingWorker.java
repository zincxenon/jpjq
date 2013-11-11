package com.github.dreambrother.jpjq.worker;

import com.github.dreambrother.jpjq.executor.JobExecutor;
import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.storage.JobStorage;

import java.util.List;

public class StartingWorker {

    private JobStorage jobStorage;
    private JobExecutor jobExecutor;

    public void start() {
        List<? extends Job> initialJobs = jobStorage.findInitial();
        for (Job job : initialJobs) {
            jobExecutor.execute(job);
        }

        List<? extends Job> inProgressJobs = jobStorage.findInProgress();
        for (Job job : inProgressJobs) {
            jobExecutor.execute(job);
        }
    }

    public void setJobStorage(JobStorage jobStorage) {
        this.jobStorage = jobStorage;
    }

    public void setJobExecutor(JobExecutor jobExecutor) {
        this.jobExecutor = jobExecutor;
    }
}
