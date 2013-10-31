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

        verify(jobStorageMock).persist(job);
    }

    @Test
    public void shouldCancelJob() {
        long id = 1;

        sut.cancel(id);

        verify(jobStorageMock).remove(id);
    }
}
