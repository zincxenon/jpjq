package com.github.dreambrother.jpjq.executor;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.SimpleJob;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PooledJobExecutorTest {

    private PooledJobExecutor sut = new PooledJobExecutor();

    @Mock
    private JobExecutor jobExecutorMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        sut.setDelegate(jobExecutorMock);
        sut.setPoolSize(2);
    }

    @Test
    public void shouldExecuteJob() {
        Job job = new SimpleJob();

        sut.execute(job);

        verify(jobExecutorMock).execute(job);
    }

    @Test
    public void shouldExecuteManyJobs() {
        int executionCount = 10;
        Job job = new SimpleJob();

        for(int i = 0; i < executionCount; i++) {
            sut.execute(job);
        }

        verify(jobExecutorMock, times(executionCount)).execute(job);
    }
}
