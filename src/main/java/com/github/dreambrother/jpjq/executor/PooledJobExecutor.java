package com.github.dreambrother.jpjq.executor;

import com.github.dreambrother.jpjq.job.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PooledJobExecutor implements JobExecutor {

    private static final Logger logger = LoggerFactory.getLogger(PooledJobExecutor.class);

    private JobExecutor jobExecutor;
    private ExecutorService executorService;

    @Override
    public void execute(Job job) {
        Future<?> future = executorService.submit(runnableJob(job));
        try {
            future.get();
        } catch (InterruptedException e) {
            logger.info("Thread {} was interrupted", Thread.currentThread());
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            logger.error("PooledJobExecutor delegate should not throw exceptions!");
        }
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

    public void setPoolSize(int poolSize) {
        executorService = Executors.newFixedThreadPool(poolSize);
    }
}
