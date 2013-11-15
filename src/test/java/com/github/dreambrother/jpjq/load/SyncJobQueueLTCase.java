package com.github.dreambrother.jpjq.load;

import com.github.dreambrother.jload.annotations.LoadTest;
import com.github.dreambrother.jpjq.JobQueue;

import static com.github.dreambrother.jpjq.job.JobBuilder.initialJob;
import static com.github.dreambrother.jpjq.utils.FileTestUtils.createTmpDir;

public class SyncJobQueueLTCase {

    private JobQueue sut = JobQueue.builder(createTmpDir()).sync().build();

    @LoadTest(iterationCount = 1000, threadCount = 1)
    public void testOn1000IterationsWith1Thread() {
        sut.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 1000, threadCount = 4)
    public void testOn1000IterationsWith4Threads() {
        sut.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 1000, threadCount = 16)
    public void testOn1000IterationsWith16Threads() {
        sut.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 100000, threadCount = 1)
    public void testOn100000IterationsWith1Thread() {
        sut.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 100000, threadCount = 4)
    public void testOn100000IterationsWith4Threads() {
        sut.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 100000, threadCount = 16)
    public void testOn100000IterationsWith16Threads() {
        sut.enqueue(initialJob());
    }
}
