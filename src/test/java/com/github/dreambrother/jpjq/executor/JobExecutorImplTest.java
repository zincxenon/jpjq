package com.github.dreambrother.jpjq.executor;

import com.github.dreambrother.jpjq.answer.WithExceptionsAndThenNothing;
import com.github.dreambrother.jpjq.job.*;
import com.github.dreambrother.jpjq.service.DelayService;
import com.github.dreambrother.jpjq.visitor.ExecutorJobVisitor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JobExecutorImplTest {

    private JobExecutorImpl sut = new JobExecutorImpl();
    private ExecutorJobVisitor jobVisitor = new ExecutorJobVisitor();

    @Mock
    private Runnable mock;
    @Mock
    private DelayService delayServiceMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        jobVisitor.setDelayService(delayServiceMock);
        sut.setJobVisitor(jobVisitor);
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

    @Test
    public void shouldRetryToExecuteAfterExceptionInRetryJob() {
        RetryJob job = new RetryJob(2) {
            public void execute() {
                mock.run();
            }
        };

        doAnswer(withExceptionsAndThenNothing(1)).when(mock).run();
        sut.execute(job);

        assertEquals(JobStatus.DONE, job.getJobStatus());
    }

    @Test
    public void shouldFailRetryJobWhenRetryAttemptsAreExceeded() {
        RetryJob job = new RetryJob(2) {
            public void execute() {
                mock.run();
            }
        };

        doThrow(new RuntimeException("That's ok")).when(mock).run();
        sut.execute(job);

        verify(mock, times(job.getAttemptCount())).run();
        assertEquals(JobStatus.FAILED, job.getJobStatus());
    }

    @Test
    public void shouldWaitAndRetryToExecuteAfterExceptionInRetryWithDelayJob() {
        RetryWithDelayJob job = new RetryWithDelayJob(1000, 2) {
            public void execute() {
                mock.run();
            }
        };

        doAnswer(withExceptionsAndThenNothing(1)).when(mock).run();
        sut.execute(job);

        verify(mock, times(job.getAttemptCount())).run();
        verify(delayServiceMock, times(1)).delay(job.getDelay());
    }

    private Answer<Void> withExceptionsAndThenNothing(int exceptionsCount) {
        return new WithExceptionsAndThenNothing(exceptionsCount);
    }
}
