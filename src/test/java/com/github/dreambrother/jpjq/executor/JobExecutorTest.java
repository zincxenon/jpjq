package com.github.dreambrother.jpjq.executor;

import com.github.dreambrother.jpjq.job.Job;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

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
        Job job = new Job() {
            public void execute() {
                mock.run();
            }
        };

        sut.execute(job);

        verify(mock).run();
    }
}
