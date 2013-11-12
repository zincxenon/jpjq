package com.github.dreambrother.jpjq.builder;

import com.github.dreambrother.jpjq.JobQueue;
import com.github.dreambrother.jpjq.executor.JobExecutor;
import com.github.dreambrother.jpjq.executor.JobExecutorImpl;
import com.github.dreambrother.jpjq.generator.JobFileNameGenerator;
import com.github.dreambrother.jpjq.service.DelayService;
import com.github.dreambrother.jpjq.service.DelayServiceImpl;
import com.github.dreambrother.jpjq.storage.FileJobStorage;
import com.github.dreambrother.jpjq.storage.JobStorage;
import com.github.dreambrother.jpjq.visitor.ExecutorJobVisitor;
import com.github.dreambrother.jpjq.visitor.JobVisitor;

import java.io.File;

public class JobQueueBuilder {

    private File queueDir;

    public JobQueueBuilder(File queueDir) {
        this.queueDir = queueDir;
    }

    public JobQueue build() {
        JobQueue jobQueue = new JobQueue();

        JobStorage jobStorage = createJobStorage();
        jobQueue.setJobStorage(jobStorage);

        JobExecutor jobExecutor = createJobExecutor(jobStorage);
        jobQueue.setJobExecutor(jobExecutor);

        return jobQueue;
    }

    private JobStorage createJobStorage() {
        FileJobStorage jobStorage = new FileJobStorage(queueDir);
        jobStorage.setFileNameGenerator(new JobFileNameGenerator());
        return jobStorage;
    }

    private JobExecutor createJobExecutor(JobStorage jobStorage) {
        JobExecutorImpl jobExecutor = new JobExecutorImpl();
        jobExecutor.setJobStorage(jobStorage);
        jobExecutor.setJobVisitor(createJobVisitor());
        return jobExecutor;
    }

    private JobVisitor createJobVisitor() {
        ExecutorJobVisitor jobVisitor = new ExecutorJobVisitor();
        jobVisitor.setDelayService(createDelayService());
        return jobVisitor;
    }

    private DelayService createDelayService() {
        return new DelayServiceImpl();
    }
}
