package com.github.dreambrother.jpjq.generator

import com.github.dreambrother.jpjq.job.Job
import com.github.dreambrother.jpjq.job.SimpleJob
import org.joda.time.Instant
import org.joda.time.format.DateTimeFormat
import org.junit.Test

import static org.junit.Assert.assertEquals

public class JobFileNameGeneratorTest {

    private JobFileNameGenerator sut = new JobFileNameGenerator();

    @Test
    public void shouldGenerateValidJobName() {
        Job job = new SimpleJob();
        job.setId("123");
        job.setCreationInstant(Instant.parse("20130101 UTC", DateTimeFormat.forPattern("yyyyMMdd ZZZ")));

        String actual = sut.generate(job);

        assertEquals("20130101-123", actual);
    }
}
