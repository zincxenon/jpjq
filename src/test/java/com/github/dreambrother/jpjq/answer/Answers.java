package com.github.dreambrother.jpjq.answer;

import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.JobStatus;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;

public class Answers {

    public static Answer<Void> assertStatusEq(final JobStatus expected, final Job job) {
        return new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) throws Throwable {
                assertEquals(expected, job.getJobStatus());
                return null;
            }
        };
    }

    public static Answer<Void> withExceptionsAndThenNothing(final int exceptionsCount) {
        return new Answer<Void>() {
            private int counter = exceptionsCount;

            public Void answer(InvocationOnMock invocation) throws Throwable {
                if (counter == 0) {
                    return null;
                } else {
                    counter--;
                    throw new RuntimeException("That's ok");
                }
            }
        };
    }
}
