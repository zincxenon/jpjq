package com.github.dreambrother.jpjq.answer;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class WithExceptionsAndThenNothing implements Answer<Void> {

    private int exceptionsCount;

    public WithExceptionsAndThenNothing(int exceptionsCount) {
        this.exceptionsCount = exceptionsCount;
    }

    @Override
    public Void answer(InvocationOnMock invocation) throws Throwable {
        if (exceptionsCount == 0) {
            return null;
        } else {
            exceptionsCount--;
            throw new RuntimeException("That's ok");
        }
    }
}
