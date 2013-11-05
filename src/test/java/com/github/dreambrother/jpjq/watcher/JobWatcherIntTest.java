package com.github.dreambrother.jpjq.watcher;

import com.github.dreambrother.jpjq.answer.CountDownAnswer;
import com.github.dreambrother.jpjq.exceptions.WatcherNotStartedException;
import com.github.dreambrother.jpjq.executor.JobExecutor;
import com.github.dreambrother.jpjq.generator.JobFileNameGenerator;
import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.storage.FileJobStorage;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;

import static com.github.dreambrother.jpjq.job.JobBuilder.initialJob;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class JobWatcherIntTest {

    private JobWatcher sut;

    private FileJobStorage jobStorage;
    private File queueDir;

    @Mock
    private JobExecutor jobExecutorMock;

    @Before
    public void createQueueDir() throws IOException {
        MockitoAnnotations.initMocks(this);
        queueDir = Files.createDirectory(new File("target/queue").toPath()).toFile();
        jobStorage = new FileJobStorage(queueDir);
        jobStorage.setFileNameGenerator(new JobFileNameGenerator());

        sut = new JobWatcher();
        sut.setWatchDir(jobStorage.getInitDir());
        sut.setJobExecutor(jobExecutorMock);
        sut.start();
    }

    @After
    public void removeQueueDir() throws IOException {
        sut.stop();
        FileUtils.forceDelete(queueDir);
    }

    @Test
    public void shouldCallJobExecutorWhenInitialJobIsAdded() throws InterruptedException {
        Job job = initialJob();

        CountDownLatch latch = new CountDownLatch(1);
        doAnswer(countDownAnswer(latch)).when(jobExecutorMock).execute(job);

        jobStorage.store(job);

        latch.await();
        verify(jobExecutorMock).execute(job);
    }

    @Test(expected = WatcherNotStartedException.class)
    public void shouldThrowExceptionWhenStopIsCalledBeforeStart() {
        new JobWatcher().stop();
    }

    private CountDownAnswer countDownAnswer(CountDownLatch latch) {
        return new CountDownAnswer(latch);
    }
}
