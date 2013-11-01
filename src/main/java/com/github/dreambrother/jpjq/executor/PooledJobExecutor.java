package com.github.dreambrother.jpjq.executor;

import com.github.dreambrother.jpjq.job.Job;

import java.util.concurrent.Executor;

public class PooledJobExecutor implements JobExecutor {

    private JobExecutor jobExecutor;
    private Executor executor;

    @Override
    public void execute(Job job) {
        executor.execute(runnableJob(job));
    }

    private Runnable runnableJob(final Job job) {
        return new Runnable() {
            public void run() {
                jobExecutor.execute(job);
            }
        };
    }

    public void setDelegate(JobExecutor jobExecutor) {
        this.jobExecutor = jobExecutor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
}
