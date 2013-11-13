package com.github.dreambrother.jpjq.builder;

import org.joda.time.Duration;

public class JobsGcWorkerConfig {

    private Duration expirationDuration;
    private Duration delay;

    public JobsGcWorkerConfig(Duration expirationDuration, Duration delay) {
        this.expirationDuration = expirationDuration;
        this.delay = delay;
    }

    public Duration getExpirationDuration() {
        return expirationDuration;
    }

    public Duration getDelay() {
        return delay;
    }
}
