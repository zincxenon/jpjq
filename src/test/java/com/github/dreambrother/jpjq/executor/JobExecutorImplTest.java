package com.github.dreambrother.jpjq.executor;

import com.github.dreambrother.jpjq.exceptions.IllegalJobStatusException;
import com.github.dreambrother.jpjq.job.*;
import com.github.dreambrother.jpjq.service.DelayService;
import com.github.dreambrother.jpjq.storage.JobStorage;
import com.github.dreambrother.jpjq.visitor.ExecutorJobVisitor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.github.dreambrother.jpjq.answer.Answers.assertStatusEq;
import static com.github.dreambrother.jpjq.answer.Answers.withExceptionsAndThenNothing;
import static com.github.dreambrother.jpjq.job.JobBuilder.inProgressJob;
import static com.github.dreambrother.jpjq.job.JobBuilder.initialJob;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JobExecutorImplTest {

    private JobExecutorImpl sut = new JobExecutorImpl();
    private ExecutorJobVisitor jobVisitor = new ExecutorJobVisitor();

    @Mock
    private Runnable runnableMock;
    @Mock
    private DelayService delayServiceMock;
    @Mock
    private JobStorage jobStorageMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        jobVisitor.setDelayService(delayServiceMock);
        sut.setJobVisitor(jobVisitor);
        sut.setJobStorage(jobStorageMock);
    }

    @Test
    public void shouldExecuteJob() {
        Job job = new MockJob(runnableMock);

        sut.execute(job);

        verify(runnableMock).run();
        assertEquals(JobStatus.DONE, job.getJobStatus());
    }

    @Test
    public void shouldFailJobAfterException() {
        Job job = new MockJob(runnableMock);

        doThrow(RuntimeException.class).when(runnableMock).run();
        sut.execute(job);

        assertEquals(JobStatus.FAILED, job.getJobStatus());
    }

    @Test
    public void shouldSetInProgressStatusBeforeExecute() {
        final Job job = new MockJob(runnableMock);

        doAnswer(assertStatusEq(JobStatus.IN_PROGRESS, job)).when(runnableMock).run();
        sut.execute(job);
    }

    @Test
    public void shouldStoreInProgressJob() {
        Job job = initialJob();

        doAnswer(assertStatusEq(JobStatus.INITIAL, job)).when(jobStorageMock).store(job);
        sut.execute(job);
    }

    @Test(expected = IllegalJobStatusException.class)
    public void shouldNotAcceptInProgressJob() {
        sut.execute(inProgressJob());
    }

    @Test
    public void shouldRetryToExecuteAfterExceptionInRetryJob() {
        RetryJob job = new RetryJob(2) {
            public void execute() {
                runnableMock.run();
            }
        };

        doAnswer(withExceptionsAndThenNothing(1)).when(runnableMock).run();
        sut.execute(job);

        assertEquals(JobStatus.DONE, job.getJobStatus());
    }

    @Test
    public void shouldFailRetryJobWhenRetryAttemptsAreExceeded() {
        RetryJob job = new RetryJob(2) {
            public void execute() {
                runnableMock.run();
            }
        };

        doThrow(new RuntimeException("That's ok")).when(runnableMock).run();
        sut.execute(job);

        verify(runnableMock, times(job.getAttemptCount())).run();
        assertEquals(JobStatus.FAILED, job.getJobStatus());
    }

    @Test
    public void shouldWaitAndRetryToExecuteAfterExceptionInRetryWithDelayJob() {
        RetryWithDelayJob job = new RetryWithDelayJob(1000, 2) {
            public void execute() {
                runnableMock.run();
            }
        };

        doAnswer(withExceptionsAndThenNothing(1)).when(runnableMock).run();
        sut.execute(job);

        verify(runnableMock, times(job.getAttemptCount())).run();
        verify(delayServiceMock, times(1)).delay(job.getDelay());
    }
}
