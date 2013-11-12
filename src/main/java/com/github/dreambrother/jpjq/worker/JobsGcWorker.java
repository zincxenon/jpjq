package com.github.dreambrother.jpjq.worker;

import org.joda.time.Duration;

public interface JobsGcWorker extends Runnable {

    Duration getExpirationDuration();
}
