package com.github.dreambrother.jpjq.builder;

import com.github.dreambrother.jpjq.JobQueue;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.github.dreambrother.jpjq.job.JobBuilder.initialJob;

public class JobQueueBuilderSystemTest {

    private File queueDir;

    @Before
    public void init() throws IOException {
        queueDir = Files.createTempDirectory("queue").toFile();
    }

    @After
    public void destroy() throws IOException {
        FileUtils.forceDelete(queueDir);
    }

    @Test
    public void shouldBuildJobQueue() {
        JobQueue jobQueue = JobQueue.builder(queueDir).build();
        jobQueue.enqueue(initialJob());
    }
}
