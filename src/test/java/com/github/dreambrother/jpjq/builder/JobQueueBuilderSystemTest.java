package com.github.dreambrother.jpjq.builder;

import com.github.dreambrother.jpjq.JobQueue;
import com.github.dreambrother.jpjq.exceptions.IncorrectConfigurationException;
import com.github.dreambrother.jpjq.job.JobStatus;
import com.github.dreambrother.jpjq.job.SimpleJob;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.github.dreambrother.jpjq.job.JobBuilder.initialJob;
import static org.junit.Assert.assertEquals;

public class JobQueueBuilderSystemTest {

    private File queueDir;

    @Before
    public void init() throws IOException {
        queueDir = Files.createTempDirectory("queue").toFile();
    }

    @Test
    public void shouldBuildJobQueue() {
        JobQueue jobQueue = JobQueue.builder(queueDir).build();
        jobQueue.enqueue(initialJob());
    }

    @Test
    public void shouldBuildSyncJobQueue() {
        JobQueue jobQueue = JobQueue.builder(queueDir)
                .sync()
                .build();

        SimpleJob job = initialJob();
        jobQueue.enqueue(job);

        // check that job queue is really sync
        assertEquals(JobStatus.DONE, job.getJobStatus());
    }

    @Test
    public void shouldSupportThreadPoolSizeSetting() {
        JobQueue jobQueue = JobQueue.builder(queueDir)
                .poolSize(1)
                .build();
        jobQueue.enqueue(initialJob());
    }

    @Test(expected = IncorrectConfigurationException.class)
    public void shouldThrowExceptionWhenBuildSyncQueueWithSettedPoolSize() {
        JobQueue.builder(queueDir)
                .poolSize(1)
                .sync()
                .build();
    }

    @Test(expected = IncorrectConfigurationException.class)
    public void shouldThrowExceptionWhenBuildASyncQueueWithSettedSyncFlag() {
        JobQueue.builder(queueDir)
                .sync()
                .poolSize(1)
                .build();
    }

    @Test
    public void shouldSupportJobsGcWorkerScheduling() {
        Duration expirationPeriod = Duration.standardHours(1);
        Duration delay = Duration.standardSeconds(10);

        JobQueue jobQueue = JobQueue.builder(queueDir)
                .withJobsGc(new JobsGcConfig(expirationPeriod, delay))
                .build();

        jobQueue.enqueue(initialJob());
    }
}
