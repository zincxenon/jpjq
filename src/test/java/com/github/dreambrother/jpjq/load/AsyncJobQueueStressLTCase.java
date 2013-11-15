package com.github.dreambrother.jpjq.load;

import com.github.dreambrother.jload.annotations.LoadTest;
import com.github.dreambrother.jpjq.JobQueue;

import static com.github.dreambrother.jpjq.job.JobBuilder.initialJob;
import static com.github.dreambrother.jpjq.utils.FileTestUtils.createTmpDir;

public class AsyncJobQueueStressLTCase {

    private JobQueue sut = JobQueue.builder(createTmpDir()).poolSize(64).build();

    @LoadTest(iterationCount = 10000000, threadCount = 32)
    public void stressTest() {
        sut.enqueue(initialJob());
    }
}
