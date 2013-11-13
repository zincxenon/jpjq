package com.github.dreambrother.jpjq.builder;

import org.joda.time.Duration;

public class JobsGcConfig {

    private Duration expirationDuration;
    private Duration delay;

    public JobsGcConfig(Duration expirationDuration, Duration delay) {
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
