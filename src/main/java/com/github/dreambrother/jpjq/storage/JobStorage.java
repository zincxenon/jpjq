package com.github.dreambrother.jpjq.storage;

import com.github.dreambrother.jpjq.job.Job;

import java.util.List;

public interface JobStorage {

    void persist(Job job);
    void remove(long id);
    List<? extends Job> findInProgress();
    List<? extends Job> findInitial();
    Job findById(long id);
}
