package com.github.dreambrother.jpjq.worker;

import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class WorkerSchedulerTest {

    private WorkerScheduler sut = new WorkerScheduler();
    private Duration expirationDuration = Duration.standardHours(1);

    @Mock
    private ScheduledExecutorService executorServiceMock;
    @Mock
    private DoneJobsGcWorker doneJobsGcWorkerMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        sut.setScheduledExecutorService(executorServiceMock);
    }

    @Test
    public void shouldScheduleDoneJobsGcWorker() {
        when(doneJobsGcWorkerMock.getExpirationDuration()).thenReturn(expirationDuration);
        sut.scheduleDoneJobsGcWorker(doneJobsGcWorkerMock);

        verify(executorServiceMock, times(1)).scheduleWithFixedDelay(
                doneJobsGcWorkerMock,
                expirationDuration.getMillis(),
                expirationDuration.getMillis(),
                TimeUnit.MILLISECONDS);
    }
}
