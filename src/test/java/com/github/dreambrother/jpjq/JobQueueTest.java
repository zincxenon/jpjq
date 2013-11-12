package com.github.dreambrother.jpjq;

import com.github.dreambrother.jpjq.exceptions.IllegalJobStatusException;
import com.github.dreambrother.jpjq.executor.JobExecutor;
import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.SimpleJob;
import com.github.dreambrother.jpjq.storage.JobStorage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.github.dreambrother.jpjq.job.JobBuilder.*;
import static com.github.dreambrother.jpjq.job.JobBuilder.failedJob;
import static org.mockito.Mockito.verify;

public class JobQueueTest {

    private JobQueue sut = new JobQueue();

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
    public void shouldAcceptEmptyStatusJob() {
        sut.enqueue(emptyJob());
    }

    @Test
    public void shouldAcceptInitialJob() {
        sut.enqueue(initialJob());
    }

    @Test(expected = IllegalJobStatusException.class)
    public void shouldNotAcceptInProgressJob() {
        sut.enqueue(inProgressJob());
    }

    @Test(expected = IllegalJobStatusException.class)
    public void shouldNotAcceptDoneJob() {
        sut.enqueue(doneJob());
    }

    @Test(expected = IllegalJobStatusException.class)
    public void shouldNotAcceptFailedJob() {
        sut.enqueue(failedJob());
    }

    @Test
    public void shouldEnqueueJob() {
        Job job = new SimpleJob();

        sut.enqueue(job);

        verify(jobStorageMock).store(job);
        verify(jobExecutorMock).execute(job);
    }

    @Test
    public void shouldReturnInProgressJobs() {
        sut.getInProgress();

        verify(jobStorageMock).findInProgress();
    }

    @Test
    public void shouldReturnInitialJobs() {
        sut.getInitial();

        verify(jobStorageMock).findInitial();
    }

    @Test
    public void shouldReturnFailedJobs() {
        sut.getFailed();

        verify(jobStorageMock).findFailed();
    }

    @Test
    public void shouldReturnAllJobs() {
        sut.getAll();

        verify(jobStorageMock).findAll();
    }

    @Test
    public void shouldReturnDoneJobs() {
        sut.getDone();

        verify(jobStorageMock).findDone();
    }
}
