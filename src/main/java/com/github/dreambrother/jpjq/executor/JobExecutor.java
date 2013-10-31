package com.github.dreambrother.jpjq.executor;

import com.github.dreambrother.jpjq.job.Job;

public interface JobExecutor {

    void execute(Job job);
}
