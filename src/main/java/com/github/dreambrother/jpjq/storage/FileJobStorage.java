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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileJobStorage implements JobStorage {

    private ObjectMapper objectMapper = new ObjectMapper();

    private File queueDir;
    private File initDir;
    private File inProgressDir;
    private File doneDir;
    private File failedDir;

    private ValueGenerator<Job, String> fileNameGenerator;
    private Map<JobStatus, File> jobStatusToDirMapping = new HashMap<>();

    public FileJobStorage(File queueDir) {
        if (queueDir.exists() && !queueDir.isDirectory()) {
            throw new IllegalArgumentException("queueDir file must be directory!");
        }
        this.queueDir = queueDir;

        initQueueStore();
        initMappings();

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

    private void initMappings() {
        jobStatusToDirMapping.put(JobStatus.INITIAL, initDir);
        jobStatusToDirMapping.put(JobStatus.IN_PROGRESS, inProgressDir);
        jobStatusToDirMapping.put(JobStatus.DONE, doneDir);
        jobStatusToDirMapping.put(JobStatus.FAILED, failedDir);
    }

    @Override
    public void store(Job job) {
        String fileName = fileNameGenerator.generate(job);
        File jobDir = getJobDir(job.getJobStatus());
        try {
            storeJob(job, fileName, jobDir);
        } catch (IOException ex) {
            throw new JobStoreException(ex);
        }
    }

    private File getJobDir(JobStatus jobStatus) {
        File jobDir = jobStatusToDirMapping.get(jobStatus);
        if (jobDir == null) {
            throw new UnsupportedOperationException("Store job with status " + jobStatus + " is unsupported");
        }
        return jobDir;
    }

    private void storeJob(Job job, String fileName, File toDir) throws IOException {
        File jobFile = new File(toDir, fileName);
        objectMapper.writeValue(jobFile, job);
    }

    @Override
    public void remove(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Job> findInitial() {
        return allInFolder(initDir);
    }

    @Override
    public List<? extends Job> findInProgress() {
        return allInFolder(inProgressDir);
    }

    @Override
    public List<? extends Job> findDone() {
        return allInFolder(doneDir);
    }

    @Override
    public List<? extends Job> findFailed() {
        return allInFolder(failedDir);
    }

    @Override
    public List<? extends Job> findAll() {
        throw new UnsupportedOperationException();
    }

    private List<? extends Job> allInFolder(File dir) {
        return Lists.transform(Arrays.asList(dir.listFiles()), new Function<File, Job>() {
            public Job apply(File file) {
                try {
                    return objectMapper.readValue(file, Job.class);
                } catch (IOException ex) {
                    throw new JobReadException(ex);
                }
            }
        });
    }

    public void setFileNameGenerator(ValueGenerator<Job, String> fileNameGenerator) {
        this.fileNameGenerator = fileNameGenerator;
    }
}
