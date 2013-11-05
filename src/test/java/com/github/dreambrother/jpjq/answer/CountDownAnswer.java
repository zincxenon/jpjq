package com.github.dreambrother.jpjq.answer;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.CountDownLatch;

public class CountDownAnswer implements Answer<Void> {

    private CountDownLatch latch;

    public CountDownAnswer(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public Void answer(InvocationOnMock invocation) throws Throwable {
        latch.countDown();
        return null;
    }
}
