package com.github.dreambrother.jpjq.worker;

import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.verify;

public class WorkerSchedulerTest {

    private WorkerScheduler sut = new WorkerScheduler();
    private Duration delay = Duration.millis(100);

    @Mock
    private ScheduledExecutorService executorServiceMock;
    @Mock
    private JobsGcWorker jobsGcWorkerMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        sut.setScheduledExecutorService(executorServiceMock);
    }

    @Test
    public void shouldScheduleJobsGcWorker() {
        sut.scheduleJobsGcWorker(jobsGcWorkerMock, delay);

        verify(executorServiceMock).scheduleAtFixedRate(
                jobsGcWorkerMock,
                delay.getMillis(),
                delay.getMillis(),
                TimeUnit.MILLISECONDS);
    }
}
