package com.github.dreambrother.jpjq;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.storage.JobStorage;

import java.util.List;

public class JobQueue {

    private JobStorage jobStorage;

    public void enqueue(Job job) {
        jobStorage.persist(job);
    }

    public void cancel(long id) {
        jobStorage.remove(id);
    }

    public void setJobStorage(JobStorage jobStorage) {
        this.jobStorage = jobStorage;
    }

    public List<? extends Job> getInProgress() {
        return jobStorage.findInProgress();
    }

    public List<? extends Job> getInitial() {
        return jobStorage.findInitial();
    }

    public Job getById(long id) {
        return jobStorage.findById(id);
    }
}
