package com.github.dreambrother.jpjq;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.storage.JobStorage;

import java.util.List;

public class JobQueue {

    private JobStorage jobStorage;

    public void enqueue(Job job) {
        jobStorage.store(job);
    }

    public void cancel(long id) {
        jobStorage.remove(id);
    }

    public Job getById(long id) {
        return jobStorage.findById(id);
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
}
