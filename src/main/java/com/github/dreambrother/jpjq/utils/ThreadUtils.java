package com.github.dreambrother.jpjq.utils;

import org.slf4j.Logger;

public class ThreadUtils {

    private ThreadUtils() {}

    public static void interruptCurrentThreadWithLog(Logger logger) {
        logger.info("Thread {} was interrupted", Thread.currentThread());
        Thread.currentThread().interrupt();
    }
}
