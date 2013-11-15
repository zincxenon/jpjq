package com.github.dreambrother.jpjq.load;

import com.github.dreambrother.jload.annotations.LoadTest;
import com.github.dreambrother.jpjq.JobQueue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.github.dreambrother.jpjq.job.JobBuilder.initialJob;

public class JobQueueSyncLTCase {

    private JobQueue sut = newSyncJobQueue();

    @LoadTest(iterationCount = 1000, threadCount = 1)
    public void testOnThousandIterationsWithSingleThread() {
        sut.enqueue(initialJob());
    }

    private JobQueue newSyncJobQueue() {
        return JobQueue.builder(createTmpDir())
                .sync()
                .build();
    }

    private File createTmpDir() {
        try {
            return Files.createTempDirectory("queue-lt").toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
