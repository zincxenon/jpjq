package com.github.dreambrother.jpjq;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.SimpleJob;
import com.github.dreambrother.jpjq.storage.JobStorage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.answers.Returns;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    public void shouldReturnInProgressJobs() {
        List<? extends Job> jobs = Arrays.asList(new SimpleJob());

        when(jobStorageMock.findInProgress()).thenAnswer(new Returns(jobs));
        List<? extends Job> actual = sut.getInProgress();

        assertEquals(jobs, actual);
    }

    @Test
    public void shouldReturnInitialJobs() {
        List<? extends Job> jobs = Arrays.asList(new SimpleJob());

        when(jobStorageMock.findInitial()).thenAnswer(new Returns(jobs));
        List<? extends Job> actual = sut.getInitial();

        assertEquals(jobs, actual);
    }

    @Test
    public void shouldReturnJobById() {
        Job job = new SimpleJob();

        when(jobStorageMock.findById(1L)).thenReturn(job);
        Job actual = sut.getById(1L);

        assertEquals(job, actual);
    }
}
