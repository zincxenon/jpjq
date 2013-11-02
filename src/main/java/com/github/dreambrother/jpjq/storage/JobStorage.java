package com.github.dreambrother.jpjq.storage;

import com.github.dreambrother.jpjq.job.Job;

import java.util.List;

public interface JobStorage {

    void store(Job job);
    void remove(String id);
    List<? extends Job> findInProgress();
    List<? extends Job> findInitial();
    List<? extends Job> findFailed();
    List<? extends Job> findAll();
    List<? extends Job> findDone();
}
