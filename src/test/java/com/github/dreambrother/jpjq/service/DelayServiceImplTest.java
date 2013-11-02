package com.github.dreambrother.jpjq.service;

import org.junit.Test;

import static com.github.dreambrother.jpjq.matcher.MatcherUtils.greaterThan;
import static org.junit.Assert.assertThat;

public class DelayServiceImplTest {

    private DelayServiceImpl sut = new DelayServiceImpl();

    @Test
    public void testDelay() {
        long delay = 500L;
        long eps = 50L;

        long startMillis = System.currentTimeMillis();
        sut.delay(delay);
        long duration = System.currentTimeMillis() - startMillis;

        assertThat(duration + eps, greaterThan(delay));
    }
}
