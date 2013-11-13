package com.github.dreambrother.jpjq.builder;

import com.github.dreambrother.jpjq.JobQueue;
import com.github.dreambrother.jpjq.exceptions.IncorrectConfigurationException;
import com.github.dreambrother.jpjq.executor.JobExecutor;
import com.github.dreambrother.jpjq.executor.JobExecutorImpl;
import com.github.dreambrother.jpjq.executor.PooledJobExecutor;
import com.github.dreambrother.jpjq.generator.JobFileNameGenerator;
import com.github.dreambrother.jpjq.service.DelayService;
import com.github.dreambrother.jpjq.service.DelayServiceImpl;
import com.github.dreambrother.jpjq.storage.FileJobStorage;
import com.github.dreambrother.jpjq.storage.JobStorage;
import com.github.dreambrother.jpjq.visitor.ExecutorJobVisitor;
import com.github.dreambrother.jpjq.visitor.JobVisitor;
import com.github.dreambrother.jpjq.worker.DoneJobsGcWorker;
import com.github.dreambrother.jpjq.worker.FailedJobsGcWorker;
import com.github.dreambrother.jpjq.worker.StartingWorker;
import com.github.dreambrother.jpjq.worker.WorkerScheduler;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class JobQueueBuilder {

    private File queueDir;
    private JobStorage jobStorage;
    private JobExecutor jobExecutor;
    private JobVisitor jobVisitor;
    private DelayService delayService;
    private JobsGcWorkerConfig jobsGcWorkerConfig;

    public JobQueueBuilder(File queueDir) {
        this.queueDir = queueDir;
    }

    public JobQueueBuilder sync() {
        if (jobExecutor != null) {
            throw new IncorrectConfigurationException("Sync/async switch already configured!");
        }
        jobExecutor = createJobExecutor();
        return this;
    }

    public JobQueueBuilder poolSize(int threadPoolSize) {
        if (jobExecutor != null) {
            throw new IncorrectConfigurationException("Sync/async switch already configured!");
        }
        jobExecutor = createPooledJobExecutor(threadPoolSize);
        return this;
    }

    public JobQueueBuilder withJobsGc(JobsGcWorkerConfig jobsGcWorkerConfig) {
        this.jobsGcWorkerConfig = jobsGcWorkerConfig;
        return this;
    }

    public JobQueue build() {
        JobQueue jobQueue = new JobQueue();

        jobQueue.setJobStorage(getJobStorage());
        if (jobExecutor == null) {
            jobExecutor = createPooledJobExecutor(16);
        }
        jobQueue.setJobExecutor(jobExecutor);

        runStartingWorker(jobExecutor);
        scheduleJobsGcWorkersIfNecessary();

        return jobQueue;
    }

    private void runStartingWorker(JobExecutor jobExecutor) {
        StartingWorker startingWorker = new StartingWorker();
        startingWorker.setJobStorage(getJobStorage());
        startingWorker.setJobExecutor(jobExecutor);
        startingWorker.run();
    }

    private void scheduleJobsGcWorkersIfNecessary() {
        if (jobsGcWorkerConfig != null) {
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
            WorkerScheduler workerScheduler = new WorkerScheduler();
            workerScheduler.setScheduledExecutorService(executorService);

            workerScheduler.scheduleJobsGcWorker(createDoneJobsGcWorker(), jobsGcWorkerConfig.getDelay());
            workerScheduler.scheduleJobsGcWorker(createFailedJobsGcWorker(), jobsGcWorkerConfig.getDelay());
        }
    }

    private DoneJobsGcWorker createDoneJobsGcWorker() {
        DoneJobsGcWorker worker = new DoneJobsGcWorker();
        worker.setJobStorage(getJobStorage());
        worker.setExpirationDuration(jobsGcWorkerConfig.getExpirationDuration());
        return worker;
    }

    private FailedJobsGcWorker createFailedJobsGcWorker() {
        FailedJobsGcWorker worker = new FailedJobsGcWorker();
        worker.setJobStorage(getJobStorage());
        worker.setExpirationDuration(jobsGcWorkerConfig.getExpirationDuration());
        return worker;
    }

    private JobStorage getJobStorage() {
        if (jobStorage == null) {
            FileJobStorage fileJobStorage = new FileJobStorage(queueDir);
            fileJobStorage.setFileNameGenerator(new JobFileNameGenerator());
            jobStorage = fileJobStorage;
        }
        return jobStorage;
    }

    private JobExecutor createJobExecutor() {
        JobExecutorImpl jobExecutorImpl = new JobExecutorImpl();
        jobExecutorImpl.setJobStorage(getJobStorage());
        jobExecutorImpl.setJobVisitor(getJobVisitor());
        return jobExecutorImpl;
    }

    private JobExecutor createPooledJobExecutor(int threadCount) {
        PooledJobExecutor pooledJobExecutor = new PooledJobExecutor();
        pooledJobExecutor.setExecutor(Executors.newFixedThreadPool(threadCount));
        pooledJobExecutor.setDelegate(createJobExecutor());
        return pooledJobExecutor;
    }

    private JobVisitor getJobVisitor() {
        if (jobVisitor == null) {
            ExecutorJobVisitor executorJobVisitor = new ExecutorJobVisitor();
            executorJobVisitor.setDelayService(getDelayService());
            jobVisitor = executorJobVisitor;
        }
        return jobVisitor;
    }

    private DelayService getDelayService() {
        if (delayService == null) {
            delayService = new DelayServiceImpl();
        }
        return delayService;
    }
}
