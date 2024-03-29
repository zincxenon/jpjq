package com.github.dreambrother.jpjq.worker;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.storage.JobStorage;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.github.dreambrother.jpjq.job.JobBuilder.doneJob;
import static org.mockito.Mockito.*;

public class DoneJobsGcWorkerTest extends JobsGcWorkerTestSupport {

    private DoneJobsGcWorker sut = new DoneJobsGcWorker();

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

        doReturn(jobs).when(jobStorageMock).findDone();
        sut.run();

        verifyJobStorageWasCalledWith(jobs);
    }

    @Test
    public void shouldNotRemoveNotExpiredJobs() {
        List<? extends Job> jobs = getValidJobs();

        doReturn(jobs).when(jobStorageMock).findDone();
        sut.run();

        verify(jobStorageMock, times(1)).findDone();
        verifyNoMoreInteractions(jobStorageMock);
    }

    @Override
    protected Duration getExpirationDuration() {
        return Duration.standardHours(1);
    }

    @Override
    protected Job getConcreteJob() {
        return doneJob();
    }

    @Override
    protected JobStorage getJobStorageMock() {
        return jobStorageMock;
    }
}
