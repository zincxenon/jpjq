package com.github.dreambrother.jpjq.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dreambrother.jpjq.exceptions.JobReadException;
import com.github.dreambrother.jpjq.exceptions.JobStoreException;
import com.github.dreambrother.jpjq.job.Job;

import java.io.File;
import java.io.IOException;

public class JobFileUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    }

    private JobFileUtils() {}

    public static void write(Job job, File target) {
        try {
            objectMapper.writeValue(target, job);
        } catch (IOException ex) {
            throw new JobStoreException(ex);
        }
    }

    public static Job read(File from) {
        try {
            return objectMapper.readValue(from, Job.class);
        } catch (IOException ex) {
            throw new JobReadException(ex);
        }
    }
}
