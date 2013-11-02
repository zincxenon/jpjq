package com.github.dreambrother.jpjq.generator;

import com.github.dreambrother.jpjq.job.Job;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JobFileNameGenerator implements ValueGenerator<Job, String> {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd.kk-mm-ss-SSS");

    @Override
    public String generate(Job job) {
        return job.getCreationInstant().toString(DATE_TIME_FORMATTER) + "." + job.getId();
    }
}
