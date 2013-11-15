package com.github.dreambrother.jpjq.load;

import com.github.dreambrother.jload.annotations.LoadTest;
import com.github.dreambrother.jpjq.JobQueue;

import static com.github.dreambrother.jpjq.job.JobBuilder.initialJob;
import static com.github.dreambrother.jpjq.utils.FileTestUtils.createTmpDir;

public class AsyncJobQueueLTCase {

    private JobQueue sut4 = JobQueue.builder(createTmpDir()).poolSize(4).build();
    private JobQueue sut16 = JobQueue.builder(createTmpDir()).poolSize(16).build();
    private JobQueue sut32 = JobQueue.builder(createTmpDir()).poolSize(32).build();

    @LoadTest(iterationCount = 1000, threadCount = 1)
    public void test4ThreadQueueOn1000IterationsWith1Thread() {
        sut4.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 1000, threadCount = 4)
    public void test4ThreadQueueOn1000IterationsWith4Threads() {
        sut4.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 1000, threadCount = 16)
    public void test4ThreadQueueOn1000IterationsWith16Threads() {
        sut4.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 100000, threadCount = 1)
    public void test4ThreadQueueOn100000IterationsWith1Thread() {
        sut4.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 100000, threadCount = 4)
    public void test4ThreadQueueOn100000IterationsWith4Threads() {
        sut4.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 100000, threadCount = 16)
    public void test4ThreadQueueOn100000IterationsWith16Threads() {
        sut4.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 1000, threadCount = 1)
    public void test16ThreadQueueOn1000IterationsWith1Thread() {
        sut16.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 1000, threadCount = 4)
    public void test16ThreadQueueOn1000IterationsWith4Threads() {
        sut16.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 1000, threadCount = 16)
    public void test16ThreadQueueOn1000IterationsWith16Threads() {
        sut16.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 100000, threadCount = 1)
    public void test16ThreadQueueOn100000IterationsWith1Thread() {
        sut16.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 100000, threadCount = 4)
    public void test16ThreadQueueOn100000IterationsWith4Threads() {
        sut16.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 100000, threadCount = 16)
    public void test16ThreadQueueOn100000IterationsWith16Threads() {
        sut16.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 1000, threadCount = 1)
    public void test32ThreadQueueOn1000IterationsWith1Thread() {
        sut32.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 1000, threadCount = 4)
    public void test32ThreadQueueOn1000IterationsWith4Threads() {
        sut32.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 1000, threadCount = 16)
    public void test32ThreadQueueOn1000IterationsWith16Threads() {
        sut32.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 100000, threadCount = 1)
    public void test32ThreadQueueOn100000IterationsWith1Thread() {
        sut32.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 100000, threadCount = 4)
    public void test32ThreadQueueOn100000IterationsWith4Threads() {
        sut32.enqueue(initialJob());
    }

    @LoadTest(iterationCount = 100000, threadCount = 16)
    public void test32ThreadQueueOn100000IterationsWith16Threads() {
        sut32.enqueue(initialJob());
    }
}
