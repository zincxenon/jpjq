package com.github.dreambrother.jpjq;

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
        Job job = new Job();

        sut.enqueue(job);

        verify(jobStorageMock).persist(job);
    }
}
