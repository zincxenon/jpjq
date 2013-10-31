package com.github.dreambrother.jpjq.executor;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.JobStatus;
import com.github.dreambrother.jpjq.job.MockJob;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JobExecutorTest {

    private JobExecutor sut = new JobExecutor();

    @Mock
    private Runnable mock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldExecuteJob() {
        Job job = new MockJob(mock);

        sut.execute(job);

        assertEquals(JobStatus.DONE, job.getJobStatus());
        verify(mock).run();
    }

    @Test
    public void shouldFailJobAfterException() {
        Job job = new MockJob(mock);

        doThrow(RuntimeException.class).when(mock).run();
        sut.execute(job);

        assertEquals(JobStatus.FAILED, job.getJobStatus());
    }

    @Test
    public void shouldSetInProgressStatusBeforeExecute() {
        final Job job = new MockJob(mock);

        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                assertEquals(JobStatus.IN_PROGRESS, job.getJobStatus());
                return null;
            }
        }).when(mock).run();

        sut.execute(job);
    }
}
