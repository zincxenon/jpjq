package com.github.dreambrother.jpjq.storage;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.JobStatus;

import java.util.List;

public interface JobStorage {

    void store(Job job);
    void remove(String id);
    void remove(Job job);

    List<? extends Job> findInProgress();
    List<? extends Job> findInitial();
    List<? extends Job> findFailed();
    List<? extends Job> findAll();
    List<? extends Job> findDone();

    void moveToInProgress(Job job);
    void moveToDone(Job job);
    void moveToFailed(Job job);

    void changeJobStatus(Job job, JobStatus jobStatus);
}
