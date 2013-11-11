package com.github.dreambrother.jpjq.worker;

import com.github.dreambrother.jpjq.executor.JobExecutor;
import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.storage.JobStorage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static com.github.dreambrother.jpjq.job.JobBuilder.inProgressJob;
import static com.github.dreambrother.jpjq.job.JobBuilder.initialJob;
import static org.mockito.Mockito.*;

public class StartingWorkerTest {

    private StartingWorker sut = new StartingWorker();

    @Mock
    private JobStorage jobStorageMock;
    @Mock
    private JobExecutor jobExecutorMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        sut.setJobStorage(jobStorageMock);
        sut.setJobExecutor(jobExecutorMock);
    }

    @Test
    public void shouldStartInitJobsAfterStart() {
        List<? extends Job> jobs = Arrays.asList(initialJob(), initialJob());

        doReturn(jobs).when(jobStorageMock).findInitial();
        sut.run();

        verifyJobExecutorWasCalledWith(jobs);
    }

    @Test
    public void shouldStartInProgressJobsAfterStart() {
        List<? extends Job> jobs = Arrays.asList(inProgressJob(), inProgressJob());

        doReturn(jobs).when(jobStorageMock).findInProgress();
        sut.run();

        verifyJobExecutorWasCalledWith(jobs);
    }

    private void verifyJobExecutorWasCalledWith(List<? extends Job> jobs) {
        for (Job job : jobs) {
            verify(jobExecutorMock, times(1)).execute(job);
        }
    }
}
