package com.github.dreambrother.jpjq.storage;

import com.github.dreambrother.jpjq.Job;

public interface JobStorage {

    void persist(Job job);
    void remove(long id);
}
