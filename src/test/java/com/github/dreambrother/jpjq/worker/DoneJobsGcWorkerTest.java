package com.github.dreambrother.jpjq.worker;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.storage.JobStorage;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static com.github.dreambrother.jpjq.job.JobBuilder.doneJob;
import static org.mockito.Mockito.*;

public class DoneJobsGcWorkerTest {

    private Duration expirationDuration = Duration.standardHours(1);
    long expiredCreationTimeMillis = Instant.now().getMillis() - (expirationDuration.getMillis() + 1);

    private DoneJobsGcWorker sut = new DoneJobsGcWorker();

    @Mock
    private JobStorage jobStorageMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        sut.setExpirationDuration(expirationDuration);
        sut.setJobStorage(jobStorageMock);
    }

    @Test
    public void shouldRemoveExpiredDoneJobs() {
        Job expiredJobOne = doneJob();
        Job expiredJobTwo = doneJob();
        expiredJobOne.setCreationInstant(new Instant(expiredCreationTimeMillis));
        expiredJobTwo.setCreationInstant(new Instant(expiredCreationTimeMillis));
        List<? extends Job> jobs = Arrays.asList(expiredJobOne, expiredJobTwo);

        doReturn(jobs).when(jobStorageMock).findDone();
        sut.run();

        verifyJobStorageWasCalledWith(jobs);
    }

    @Test
    public void shouldNotRemoveNotExpiredJobs() {
        Job notExpiredJobOne = doneJob();
        Job notExpiredJobTwo = doneJob();
        List<? extends Job> jobs = Arrays.asList(notExpiredJobOne, notExpiredJobTwo);

        doReturn(jobs).when(jobStorageMock).findDone();
        sut.run();

        verify(jobStorageMock, times(1)).findDone();
        verifyNoMoreInteractions(jobStorageMock);
    }

    private void verifyJobStorageWasCalledWith(List<? extends Job> jobs) {
        for (Job job : jobs) {
            verify(jobStorageMock, times(1)).remove(job);
        }
    }
}
