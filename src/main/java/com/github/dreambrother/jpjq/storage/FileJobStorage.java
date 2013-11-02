package com.github.dreambrother.jpjq.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dreambrother.jpjq.exceptions.JobReadException;
import com.github.dreambrother.jpjq.exceptions.JobStoreException;
import com.github.dreambrother.jpjq.generator.ValueGenerator;
import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.JobStatus;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileJobStorage implements JobStorage {

    private ObjectMapper objectMapper = new ObjectMapper();

    private File queueDir;
    private File initDir;
    private File inProgressDir;
    private File doneDir;
    private File failedDir;

    private ValueGenerator<Job, String> fileNameGenerator;

    public FileJobStorage(File queueDir) {
        if (queueDir.exists() && !queueDir.isDirectory()) {
            throw new IllegalArgumentException("queueDir file must be directory!");
        }
        this.queueDir = queueDir;
        initQueueStore();
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    }

    private void initQueueStore() {
        if (!queueDir.exists()) {
            queueDir.mkdir();
        }

        initDir = new File(queueDir, "initial");
        if (!initDir.exists()) {
            initDir.mkdir();
        }

        inProgressDir = new File(queueDir, "inprogress");
        if (!inProgressDir.exists()) {
            inProgressDir.mkdir();
        }

        doneDir = new File(queueDir, "done");
        if (!doneDir.exists()) {
            doneDir.mkdir();
        }

        failedDir = new File(queueDir, "failed");
        if (!failedDir.exists()) {
            failedDir.mkdir();
        }
    }

    @Override
    public void store(Job job) {
        String fileName = fileNameGenerator.generate(job);
        try {
            if (job.getJobStatus() == JobStatus.INITIAL) {
                storeInitial(job, fileName);
            }
        } catch (IOException ex) {
            throw new JobStoreException(ex);
        }
    }

    private void storeInitial(Job job, String fileName) throws IOException {
        File jobFile = new File(initDir, fileName);
        objectMapper.writeValue(jobFile, job);
    }

    @Override
    public void remove(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Job> findInProgress() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Job> findInitial() {
        return Lists.transform(Arrays.asList(initDir.listFiles()), new Function<File, Job>() {
            public Job apply(File file) {
                try {
                    return objectMapper.readValue(file, Job.class);
                } catch (IOException ex) {
                    throw new JobReadException(ex);
                }
            }
        });
    }

    @Override
    public List<? extends Job> findFailed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Job> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Job> findDone() {
        throw new UnsupportedOperationException();
    }

    public void setFileNameGenerator(ValueGenerator<Job, String> fileNameGenerator) {
        this.fileNameGenerator = fileNameGenerator;
    }
}
