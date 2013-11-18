package com.github.dreambrother.jpjq.json;

import com.github.dreambrother.jpjq.job.AbstractJob;
import com.github.dreambrother.jpjq.job.RetryJob;
import com.github.dreambrother.jpjq.job.RetryWithDelayJob;
import org.apache.commons.io.FileUtils;
import org.joda.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

public class JobFileUtilsIntTest {

    private File queueDir;

    @Before
    public void init() throws Exception {
        queueDir = Files.createTempDirectory("tmpQueue").toFile();
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.forceDelete(queueDir);
    }

    @Test
    public void shouldWriteAndReadAbstractJobExtendor() {
        AbstractJob job = new AbstractJobExtendor();
        File target = new File(queueDir, "abstr");

        JobFileUtils.write(job, target);

        assertEquals(job, JobFileUtils.read(target));
    }

    public static class AbstractJobExtendor extends AbstractJob {
        public void execute() {}
    }

    @Test
    public void shouldWriteAndReadRetryJobExtendor() {
        RetryJob job = new RetryJobExtendor();
        File target = new File(queueDir, "retry");

        JobFileUtils.write(job, target);

        assertEquals(job, JobFileUtils.read(target));
    }

    public static class RetryJobExtendor extends RetryJob {
        public RetryJobExtendor() {
            super(1);
        }
        public void execute() {}
    }

    @Test
    public void shouldWriteAndReadRetryWithDelayJobExtendor() {
        RetryWithDelayJob job = new RetryWithDelayJobExtendor();
        File target = new File(queueDir, "delay");

        JobFileUtils.write(job, target);

        assertEquals(job, JobFileUtils.read(target));
    }

    public static class RetryWithDelayJobExtendor extends RetryWithDelayJob {
        public RetryWithDelayJobExtendor() {
            super(Duration.millis(100), 1);
        }
        public void execute() {}
    }
}
