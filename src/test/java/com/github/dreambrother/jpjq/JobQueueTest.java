package com.github.dreambrother.jpjq;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.SimpleJob;
import com.github.dreambrother.jpjq.storage.JobStorage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class JobQueueTest {

    private JobQueue sut = new JobQueue();

    @Mock
    private JobStorage jobStorageMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        sut.setJobStorage(jobStorageMock);
    }

    @Test
    public void shouldEnqueueJob() {
        Job job = new SimpleJob();

        sut.enqueue(job);

        verify(jobStorageMock).store(job);
    }

    @Test
    public void shouldCancelJob() {
        sut.cancel(1);

        verify(jobStorageMock).remove(1);
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

    @Test
    public void shouldReturnJobById() {
        sut.getById(1);

        verify(jobStorageMock).findById(1);
    }
}
