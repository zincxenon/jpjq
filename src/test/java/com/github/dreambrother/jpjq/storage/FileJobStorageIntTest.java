package com.github.dreambrother.jpjq.storage;

import com.github.dreambrother.jpjq.generator.JobFileNameGenerator;
import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.JobStatus;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static com.github.dreambrother.jpjq.job.JobBuilder.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileJobStorageIntTest {

    private FileJobStorage sut;
    private JobFileNameGenerator generator = new JobFileNameGenerator();

    private File storeDir;
    private File tmpDir;
    private File emptyFile;

    @Before
    public void before() throws IOException {
        tmpDir = Files.createTempDirectory("tmpDir").toFile();
        emptyFile = new File(tmpDir, "empty");

        storeDir = Files.createTempDirectory("queue").toFile();
        sut = new FileJobStorage(storeDir);
        sut.setFileNameGenerator(generator);
    }

    @After
    public void after() throws IOException {
        FileUtils.forceDelete(storeDir);
        FileUtils.forceDelete(tmpDir);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionIfQueueFileIsNotDirectory() throws IOException {
        File tmp = Files.createTempFile("test", "constructor").toFile();
        try {
            new FileJobStorage(tmp);
        } finally {
            tmp.delete();
        }
    }

    @Test
    public void shouldCreateQueueStoreFolder() throws IOException {
        new FileJobStorage(emptyFile);
        assertTrue("should create queue store folder", emptyFile.exists());
    }

    @Test
    public void shouldCreateInitialSubfolder() {
        new FileJobStorage(emptyFile);
        assertTrue("should create 'initial' sub-folder", new File(emptyFile, "initial").exists());
    }

    @Test
    public void shouldCreateInProgressSubfolder() {
        new FileJobStorage(emptyFile);
        assertTrue("should create 'inprogress' sub-folder", new File(emptyFile, "inprogress").exists());
    }

    @Test
    public void shouldCreateDoneSubfolder() {
        new FileJobStorage(emptyFile);
        assertTrue("should create 'done' sub-folder", new File(emptyFile, "done").exists());
    }

    @Test
    public void shouldCreateFailedSubfolder() {
        new FileJobStorage(emptyFile);
        assertTrue("should create 'failed' sub-folder", new File(emptyFile, "failed").exists());
    }

    @Test
    public void shouldStoreAndFineInitialJobs() {
        Job first = initialJob();
        Job second = initialJob();

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

    @Test
    public void shouldFindAllJobs() {
        Job initJob = initialJob();
        Job ipJob = inProgressJob();
        Job doneJob = doneJob();
        Job failedJob = failedJob();

        storeJobs(initJob, ipJob, doneJob, failedJob);
        List<? extends Job> actual = sut.findAll();

        assertContains(actual, initJob, ipJob, doneJob, failedJob);
    }

    @Test
    public void shouldRemoveInitJobById() {
        Job job = initialJob();

        storeJobs(job);
        sut.remove(job.getId());
        List<? extends Job> actual = sut.findInitial();

        assertNotContains(actual, job);
    }

    @Test
    public void shouldRemoveInProgressJobById() {
        Job job = inProgressJob();

        storeJobs(job);
        sut.remove(job.getId());
        List<? extends Job> actual = sut.findInProgress();

        assertNotContains(actual, job);
    }

    @Test
    public void shouldRemoveDoneJobById() {
        Job job = doneJob();

        storeJobs(job);
        sut.remove(job.getId());
        List<? extends Job> actual = sut.findDone();

        assertNotContains(actual, job);
    }

    @Test
    public void shouldRemoveFailedJobById() {
        Job job = failedJob();

        storeJobs(job);
        sut.remove(job.getId());
        List<? extends Job> actual = sut.findFailed();

        assertNotContains(actual, job);
    }

    @Test
    public void shouldRemoveInitJob() {
        Job job = initialJob();

        storeJobs(job);
        sut.remove(job);
        List<? extends Job> actual = sut.findInitial();

        assertNotContains(actual, job);
    }

    @Test
    public void shouldRemoveInProgressJob() {
        Job job = inProgressJob();

        storeJobs(job);
        sut.remove(job);
        List<? extends Job> actual = sut.findInProgress();

        assertNotContains(actual, job);
    }

    @Test
    public void shouldRemoveDoneJob() {
        Job job = doneJob();

        storeJobs(job);
        sut.remove(job);
        List<? extends Job> actual = sut.findDone();

        assertNotContains(actual, job);
    }

    @Test
    public void shouldRemoveFailedJob() {
        Job job = failedJob();

        storeJobs(job);
        sut.remove(job);
        List<? extends Job> actual = sut.findFailed();

        assertNotContains(actual, job);
    }

    @Test
    public void shouldChangeJobStatus() {
        Job job = initialJob();

        storeJobs(job);
        sut.changeJobStatus(job, JobStatus.IN_PROGRESS);
        List<? extends Job> actual = sut.findInProgress();

        assertEquals(JobStatus.IN_PROGRESS, job.getJobStatus());
        assertContains(actual, job);
    }

    @Test
    public void shouldMoveJobToDone() {
        Job job = inProgressJob();

        storeJobs(job);
        sut.moveToDone(job);
        List<? extends Job> actual = sut.findDone();

        assertContains(actual, job);
    }

    @Test
    public void shouldMoveJobToFailed() {
        Job job = inProgressJob();

        storeJobs(job);
        sut.moveToFailed(job);
        List<? extends Job> actual = sut.findFailed();

        assertContains(actual, job);
    }

    private void assertContains(List<? extends Job> actual, Job... jobs) {
        for (Job job : jobs) {
            assertTrue("Should contains stored job", actual.contains(job));
        }
    }

    private void assertNotContains(List<? extends Job> actual, Job... jobs) {
        for (Job job : jobs) {
            assertTrue("Should not contains removed job", !actual.contains(job));
        }
    }

    private void storeJobs(Job... jobs) {
        for (Job job : jobs) {
            sut.store(job);
        }
    }
}
