package com.github.dreambrother.jpjq.storage;

import com.github.dreambrother.jpjq.generator.JobFileNameGenerator;
import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.job.JobStatus;
import com.github.dreambrother.jpjq.json.JobFileUtils;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

public class FileJobStorage implements JobStorage {

    private static final Logger logger = LoggerFactory.getLogger(FileJobStorage.class);

    private File queueDir;
    private File initDir;
    private File inProgressDir;
    private File doneDir;
    private File failedDir;

    private JobFileNameGenerator fileNameGenerator;

    private List<File> jobFolders;
    private Map<JobStatus, File> jobStatusToDirMapping = new HashMap<>();

    public FileJobStorage(File queueDir) {
        if (queueDir.exists() && !queueDir.isDirectory()) {
            throw new IllegalArgumentException("queueDir file must be directory!");
        }
        this.queueDir = queueDir;

        initQueueStore();
        initMappings();

        this.jobFolders = Arrays.asList(initDir, inProgressDir, doneDir, failedDir);
    }

    private void initQueueStore() {
        if (!queueDir.exists()) {
            queueDir.mkdir();
        }
        initDir = mkDirIfNotExists("initial");
        inProgressDir = mkDirIfNotExists("inprogress");
        doneDir = mkDirIfNotExists("done");
        failedDir = mkDirIfNotExists("failed");
    }

    private File mkDirIfNotExists(String dirName) {
        File dir = new File(queueDir, dirName);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }

    private void initMappings() {
        jobStatusToDirMapping.put(JobStatus.INITIAL, initDir);
        jobStatusToDirMapping.put(JobStatus.IN_PROGRESS, inProgressDir);
        jobStatusToDirMapping.put(JobStatus.DONE, doneDir);
        jobStatusToDirMapping.put(JobStatus.FAILED, failedDir);
    }

    @Override
    public void store(Job job) {
        logger.debug("About to store job {}", job);

        String fileName = getJobFileName(job);
        File jobDir = getJobDir(job.getJobStatus());
        storeJob(job, fileName, jobDir);
    }

    private File getJobDir(JobStatus jobStatus) {
        File jobDir = jobStatusToDirMapping.get(jobStatus);
        if (jobDir == null) {
            throw new UnsupportedOperationException("Store job with status " + jobStatus + " is unsupported");
        }
        return jobDir;
    }

    private void storeJob(Job job, String fileName, File toDir) {
        JobFileUtils.write(job, new File(toDir, fileName));
    }

    @Override
    public void remove(String id) {
        logger.debug("About to remove job with id {}", id);

        for (File folder : jobFolders) {
            boolean removed = removeIfNotEmpty(folder.listFiles(containsIdFilter(id)), id);
            if (removed) break;
        }
    }

    @Override
    public void remove(Job job) {
        logger.debug("About to remove job {}", job);

        String jobFileName = getJobFileName(job);
        File jobDir = getJobDir(job.getJobStatus());
        new File(jobDir, jobFileName).delete();
    }

    private boolean removeIfNotEmpty(File[] files, String id) {
        if (files.length == 0) {
            return false;
        } else {
            if (files.length > 1) {
                logger.error("Found more than one Job with same id {} !", id);
            }
            for (File file : files) {
                file.delete();
            }
            return true;
        }
    }

    private FilenameFilter containsIdFilter(final String id) {
        return new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains(id);
            }
        };
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
        List<Job> result = new ArrayList<>();
        for (File folder : jobFolders) {
            result.addAll(allInFolder(folder));
        }
        return result;
    }

    private List<? extends Job> allInFolder(File dir) {
        return Lists.transform(Arrays.asList(dir.listFiles()), readFromFileF());
    }

    private Function<File, Job> readFromFileF() {
        return new Function<File, Job>() {
            public Job apply(File file) {
                return JobFileUtils.read(file);
            }
        };
    }

    @Override
    public void changeJobStatus(Job job, JobStatus jobStatus) {
        logger.debug("Change status for job {} to {}", job, jobStatus);

        remove(job);
        job.setJobStatus(jobStatus);
        store(job);
    }

    private String getJobFileName(Job job) {
        return fileNameGenerator.generate(job);
    }

    public void setFileNameGenerator(JobFileNameGenerator fileNameGenerator) {
        this.fileNameGenerator = fileNameGenerator;
    }
}
