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

import static com.github.dreambrother.jpjq.job.JobBuilder.doneJob;
import static com.github.dreambrother.jpjq.job.JobBuilder.inProgressJob;
import static com.github.dreambrother.jpjq.job.JobBuilder.initialSimpleJob;
import static org.junit.Assert.assertEquals;
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
        Job first = initialSimpleJob("1");
        Job second = initialSimpleJob("2");

        storeJobs(first, second);

        assertEquals(first, sut.findInitial().get(0));
        assertEquals(second, sut.findInitial().get(1));
    }

    @Test
    public void shouldStoreAndFindInProgressJobs() {
        Job first = inProgressJob("1");
        Job second = inProgressJob("2");

        storeJobs(first, second);

        assertEquals(first, sut.findInProgress().get(0));
        assertEquals(second, sut.findInProgress().get(1));
    }

    @Test
    public void shouldStoreAndFindDoneJobs() {
        Job first = doneJob("1");
        Job second = doneJob("2");

        storeJobs(first, second);

        assertEquals(first, sut.findDone().get(0));
        assertEquals(second, sut.findDone().get(1));
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
