package com.github.dreambrother.jpjq.worker;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.storage.JobStorage;

import java.util.List;

public class FailedJobsGcWorker extends JobsGcWorkerSupport {

    @Override
    protected List<? extends Job> findJobs(JobStorage jobStorage) {
        return jobStorage.findFailed();
    }
}
