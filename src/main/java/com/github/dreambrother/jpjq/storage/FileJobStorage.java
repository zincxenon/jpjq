package com.github.dreambrother.jpjq.storage;

import com.github.dreambrother.jpjq.job.Job;

import java.io.File;
import java.util.List;

public class FileJobStorage implements JobStorage {

    private File queueDir;
    private File initDir;
    private File inProgressDir;
    private File doneDir;
    private File failedDir;

    public FileJobStorage(File queueDir) {
        if (queueDir.exists() && !queueDir.isDirectory()) {
            throw new IllegalArgumentException("queueDir file must be directory!");
        }
        this.queueDir = queueDir;
        initQueueStore();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Job> findInProgress() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Job> findInitial() {
        throw new UnsupportedOperationException();
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

    @Override
    public Job findById(long id) {
        throw new UnsupportedOperationException();
    }
}
