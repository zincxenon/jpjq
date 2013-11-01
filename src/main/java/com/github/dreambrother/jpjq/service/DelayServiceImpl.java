package com.github.dreambrother.jpjq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelayServiceImpl implements DelayService {

    private static final Logger logger = LoggerFactory.getLogger(DelayServiceImpl.class);

    @Override
    public void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logger.info("Thread {} was interrupted", Thread.currentThread());
            Thread.currentThread().interrupt();
        }
    }
}
