package com.github.dreambrother.jpjq.service;

import com.github.dreambrother.jpjq.utils.ThreadUtils;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelayServiceImpl implements DelayService {

    private static final Logger logger = LoggerFactory.getLogger(DelayServiceImpl.class);

    @Override
    public void delay(Duration duration) {
        try {
            Thread.sleep(duration.getMillis());
        } catch (InterruptedException e) {
            ThreadUtils.interruptCurrentThreadWithLog(logger);
        }
    }
}
