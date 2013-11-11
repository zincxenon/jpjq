package com.github.dreambrother.jpjq.worker;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.storage.JobStorage;
import org.joda.time.Duration;
import org.joda.time.Instant;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public abstract class JobsGcWorkerTestSupport {

    private Duration expirationDuration = getExpirationDuration();
    private long expiredCreationTimeMillis = Instant.now().getMillis() - (expirationDuration.getMillis() + 1);

    public List<? extends Job> getExpiredJobs() {
        Job expiredJobOne = getConcreteJob();
        Job expiredJobTwo = getConcreteJob();
        expiredJobOne.setCreationInstant(new Instant(expiredCreationTimeMillis));
        expiredJobTwo.setCreationInstant(new Instant(expiredCreationTimeMillis));
        return Arrays.asList(expiredJobOne, expiredJobTwo);
    }

    public List<? extends Job> getValidJobs() {
        Job notExpiredJobOne = getConcreteJob();
        Job notExpiredJobTwo = getConcreteJob();
        return Arrays.asList(notExpiredJobOne, notExpiredJobTwo);
    }

    public void verifyJobStorageWasCalledWith(List<? extends Job> jobs) {
        for (Job job : jobs) {
            verify(getJobStorageMock(), times(1)).remove(job);
        }
    }

    protected abstract Duration getExpirationDuration();
    protected abstract Job getConcreteJob();
    protected abstract JobStorage getJobStorageMock();
}
