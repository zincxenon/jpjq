package com.github.dreambrother.jpjq.service;

import org.joda.time.Duration;
import org.junit.Test;

import static com.github.dreambrother.jpjq.matcher.MatcherUtils.greaterThan;
import static org.junit.Assert.assertThat;

public class DelayServiceImplTest {

    private DelayServiceImpl sut = new DelayServiceImpl();

    @Test
    public void testDelay() {
        long delay = 500;
        long eps = 50;

        long startMillis = System.currentTimeMillis();
        sut.delay(Duration.millis(delay));
        long duration = System.currentTimeMillis() - startMillis;

        assertThat(duration + eps, greaterThan(delay));
    }
}
