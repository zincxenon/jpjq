package com.github.dreambrother.jpjq.executor;

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
    public void shouldMoveJobToInProgress() {
        Job job = initialJob();

        doAnswer(assertStatusEq(JobStatus.INITIAL, job)).when(jobStorageMock).changeJobStatus(job, JobStatus.IN_PROGRESS);
        sut.execute(job);

        verify(jobStorageMock).changeJobStatus(job, JobStatus.IN_PROGRESS);
    }

    @Test
    public void shouldMoveJobToDone() {
        Job job = initialJob();

        sut.execute(job);

        verify(jobStorageMock).changeJobStatus(job, JobStatus.DONE);
    }

    @Test
    public void shouldMoveJobToFailedWhenErrorOccures() {
        Job job = new MockJob(runnableMock);

        doThrow(new RuntimeException("That's ok")).when(runnableMock).run();
        sut.execute(job);

        verify(jobStorageMock).changeJobStatus(job, JobStatus.FAILED);
    }

    @Test
    public void shouldRetryToExecuteAfterExceptionInRetryJob() {
        RetryJob job = new RetryMockJob(2, runnableMock);

        doAnswer(withExceptionsAndThenNothing(1)).when(runnableMock).run();
        sut.execute(job);

        verify(jobStorageMock).changeJobStatus(job, JobStatus.DONE);
    }

    @Test
    public void shouldFailRetryJobWhenRetryAttemptsAreExceeded() {
        RetryJob job = new RetryMockJob(2, runnableMock);

        doThrow(new RuntimeException("That's ok")).when(runnableMock).run();
        sut.execute(job);

        verify(runnableMock, times(job.getAttemptCount())).run();
        verify(jobStorageMock).changeJobStatus(job, JobStatus.FAILED);
    }

    @Test
    public void shouldWaitAndRetryToExecuteAfterExceptionInRetryWithDelayJob() {
        RetryWithDelayJob job = new RetryWithDelayMockJob(1000, 2, runnableMock);

        doAnswer(withExceptionsAndThenNothing(1)).when(runnableMock).run();
        sut.execute(job);

        verify(runnableMock, times(job.getAttemptCount())).run();
        verify(delayServiceMock, times(1)).delay(job.getDelay());
    }
}
