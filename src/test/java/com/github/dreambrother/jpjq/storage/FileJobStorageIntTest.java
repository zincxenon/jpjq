package com.github.dreambrother.jpjq.storage;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.SimpleJob;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileJobStorageIntTest {

    private JobStorage sut;

    private File storeDir;

    @Before
    public void before() throws IOException {
        storeDir = Files.createTempDirectory("queue").toFile();
        sut = new FileJobStorage(storeDir);
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

    private File assertNonExistentFileAndGet(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            throw new AssertionError("File " + file.getAbsolutePath() + " already exists");
        }
        return file;
    }
}
