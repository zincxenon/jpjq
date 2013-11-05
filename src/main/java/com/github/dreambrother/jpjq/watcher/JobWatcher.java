package com.github.dreambrother.jpjq.watcher;

import com.github.dreambrother.jpjq.exceptions.WatcherNotStartedException;
import com.github.dreambrother.jpjq.executor.JobExecutor;
import com.github.dreambrother.jpjq.job.Job;
import com.github.dreambrother.jpjq.json.JobFileUtils;
import com.github.dreambrother.jpjq.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Not thread safe.
 */
public class JobWatcher {

    private static final Logger logger = LoggerFactory.getLogger(JobWatcher.class);

    private JobExecutor jobExecutor;
    private File watchDir;
    private Thread watchThread;
    private CountDownLatch startLatch;

    public void start() {
        startLatch = new CountDownLatch(1);
        watchThread = new Thread(watcherRunnable());
        watchThread.start();
        waitForRegistration();
    }

    private void waitForRegistration() {
        try {
            startLatch.await();
        } catch (InterruptedException e) {
            ThreadUtils.interruptCurrentThreadWithLog(logger);
            stop();
        }
    }

    private void notifyAboutRegistration() {
        startLatch.countDown();
    }

    public void stop() {
        if (watchThread == null) {
            throw new WatcherNotStartedException("Cannot stop not running watcher");
        }
        watchThread.interrupt();
    }

    private Runnable watcherRunnable() {
        return new Runnable() {
            public void run() {
                watch();
            }
        };
    }

    private void watch() {
        Path watchPath = watchDir.toPath();
        try (WatchService watchService = watchPath.getFileSystem().newWatchService()) {
            watchPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            notifyAboutRegistration();
            while (true) {
                WatchKey watchKey = watchService.take();
                processEvents(watchKey.pollEvents(), watchPath);
            }
        } catch (IOException e) {
            logger.error("Watcher was stopped.", e);
        } catch (InterruptedException e) {
            ThreadUtils.interruptCurrentThreadWithLog(logger);
        }
    }

    private void processEvents(List<WatchEvent<?>> events, Path watchPath) {
        for (WatchEvent<?> event : events) {
            if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
                continue;
            }
            Path newJobPath = pathEvent(event).context();
            readAndExecute(newJobPath, watchPath);
        }
    }

    private void readAndExecute(Path newJobPath, Path watchPath) {
        File jobFile = watchPath.resolve(newJobPath).toFile();
        Job job = JobFileUtils.read(jobFile);
        jobExecutor.execute(job);
    }

    @SuppressWarnings("unchecked")
    private WatchEvent<Path> pathEvent(WatchEvent<?> event) {
        return (WatchEvent<Path>) event;
    }

    public void setJobExecutor(JobExecutor jobExecutor) {
        this.jobExecutor = jobExecutor;
    }

    public void setWatchDir(File watchDir) {
        this.watchDir = watchDir;
    }
}
