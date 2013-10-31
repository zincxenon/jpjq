package com.github.dreambrother.jpjq;

import com.github.dreambrother.jpjq.storage.JobStorage;

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
}
