package com.github.dreambrother.jpjq.storage;

import com.github.dreambrother.jpjq.generator.JobFileNameGenerator;
import com.github.dreambrother.jpjq.job.Job;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static com.github.dreambrother.jpjq.job.JobBuilder.*;
import static org.junit.Assert.assertTrue;

public class FileJobStorageIntTest {

    private FileJobStorage sut;
    private JobFileNameGenerator generator = new JobFileNameGenerator();

    private File storeDir;

    @Before
    public void before() throws IOException {
        storeDir = Files.createTempDirectory("queue").toFile();
        sut = new FileJobStorage(storeDir);
        sut.setFileNameGenerator(generator);
    }

    @After
    public void after() throws IOException {
        FileUtils.forceDelete(storeDir);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionIfQueueDirIsFile() throws IOException {
        File tmp = Files.createTempFile("test", "constructor").toFile();
        try {
            new FileJobStorage(tmp);
        } finally {
            tmp.delete();
        }
    }

    @Test
    public void shouldCreateNecessaryFoldersForWork() throws IOException {
        File queueDir = assertNonExistentFileAndGet("target/tmpQueue");
        try {
            new FileJobStorage(queueDir);
            assertTrue("should create queue store folder", queueDir.exists());
            assertTrue("should create 'initial' sub-folder", new File(queueDir, "initial").exists());
            assertTrue("should create 'inprogress' sub-folder", new File(queueDir, "inprogress").exists());
            assertTrue("should create 'done' sub-folder", new File(queueDir, "done").exists());
            assertTrue("should create 'failed' sub-folder", new File(queueDir, "failed").exists());
        } finally {
            FileUtils.forceDelete(queueDir);
        }
    }

    @Test
    public void shouldStoreAndFineInitialJobs() {
        Job first = initialSimpleJob();
        Job second = initialSimpleJob();

        storeJobs(first, second);
        List<? extends Job> actual = sut.findInitial();

        assertContains(actual, first, second);
    }

    @Test
    public void shouldStoreAndFindInProgressJobs() {
        Job first = inProgressJob();
        Job second = inProgressJob();

        storeJobs(first, second);
        List<? extends Job> actual = sut.findInProgress();

        assertContains(actual, first, second);
    }

    @Test
    public void shouldStoreAndFindDoneJobs() {
        Job first = doneJob();
        Job second = doneJob();

        storeJobs(first, second);
        List<? extends Job> actual = sut.findDone();

        assertContains(actual, first, second);
    }

    @Test
    public void shouldStoreAndFindFailedJobs() {
        Job first = failedJob();
        Job second = failedJob();

        storeJobs(first, second);
        List<? extends Job> actual = sut.findFailed();

        assertContains(actual, first, second);
    }

    private void assertContains(List<? extends Job> actual, Job... jobs) {
        for (Job job : jobs) {
            assertTrue("Should contains stored job", actual.contains(job));
        }
    }

    private void storeJobs(Job... jobs) {
        for (Job job : jobs) {
            sut.store(job);
        }
    }

    private File assertNonExistentFileAndGet(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            throw new AssertionError("File " + file.getAbsolutePath() + " already exists");
        }
        return file;
    }
}
