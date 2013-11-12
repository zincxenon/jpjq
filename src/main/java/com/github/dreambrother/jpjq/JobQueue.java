package com.github.dreambrother.jpjq;

import com.github.dreambrother.jpjq.builder.JobQueueBuilder;
import com.github.dreambrother.jpjq.exceptions.IllegalJobStatusException;
import com.github.dreambrother.jpjq.executor.JobExecutor;
import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.JobStatus;
import com.github.dreambrother.jpjq.storage.JobStorage;

import java.io.File;
import java.util.List;

public class JobQueue {

    private JobStorage jobStorage;
    private JobExecutor jobExecutor;

    public void enqueue(Job job) {
        checkThatStatusIsEmptyOrInitial(job);
        jobStorage.store(job);
        jobExecutor.execute(job);
    }

    private void checkThatStatusIsEmptyOrInitial(Job job) {
        JobStatus jobStatus = job.getJobStatus();
        if (jobStatus != null && jobStatus != JobStatus.INITIAL) {
            throw new IllegalJobStatusException("Illegal job status " + jobStatus + ". Must be empty or INITIAL");
        }
    }

    public List<? extends Job> getInProgress() {
        return jobStorage.findInProgress();
    }

    public List<? extends Job> getInitial() {
        return jobStorage.findInitial();
    }

    public List<? extends Job> getFailed() {
        return jobStorage.findFailed();
    }

    public List<? extends Job> getAll() {
        return jobStorage.findAll();
    }

    public List<? extends Job> getDone() {
        return jobStorage.findDone();
    }

    public void setJobStorage(JobStorage jobStorage) {
        this.jobStorage = jobStorage;
    }

    public void setJobExecutor(JobExecutor jobExecutor) {
        this.jobExecutor = jobExecutor;
    }

    public static JobQueueBuilder builder(File queueDir) {
        return new JobQueueBuilder(queueDir);
    }
}
