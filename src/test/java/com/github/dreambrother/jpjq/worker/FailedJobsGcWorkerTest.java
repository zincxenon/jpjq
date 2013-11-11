package com.github.dreambrother.jpjq.worker;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.storage.JobStorage;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.github.dreambrother.jpjq.job.JobBuilder.failedJob;
import static org.mockito.Mockito.*;

public class FailedJobsGcWorkerTest extends JobsGcWorkerTestSupport {

    private FailedJobsGcWorker sut = new FailedJobsGcWorker();

    @Mock
    private JobStorage jobStorageMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        sut.setExpirationDuration(getExpirationDuration());
        sut.setJobStorage(jobStorageMock);
    }

    @Test
    public void shouldRemoveExpiredDoneJobs() {
        List<? extends Job> jobs = getExpiredJobs();

        doReturn(jobs).when(jobStorageMock).findFailed();
        sut.run();

        verifyJobStorageWasCalledWith(jobs);
    }

    @Test
    public void shouldNotRemoveNotExpiredJobs() {
        List<? extends Job> jobs = getValidJobs();

        doReturn(jobs).when(jobStorageMock).findFailed();
        sut.run();

        verify(jobStorageMock, times(1)).findFailed();
        verifyNoMoreInteractions(jobStorageMock);
    }

    @Override
    protected Duration getExpirationDuration() {
        return Duration.standardHours(1);
    }

    @Override
    protected Job getConcreteJob() {
        return failedJob();
    }

    @Override
    protected JobStorage getJobStorageMock() {
        return jobStorageMock;
    }
}
