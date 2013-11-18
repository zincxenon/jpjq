JPJQ
====

Java Persistent Job Queue

[![Build Status](https://travis-ci.org/dreambrother/jpjq.png?branch=master)](https://travis-ci.org/dreambrother/jpjq)

Status
------
Development

Usage
------
If your application execute many heavyweight asynchronous jobs and you want this jobs to continue execute after restart - this library is what you need :).

Examples
--------
### Simplest configuration for JPJQ looks like
```java
JobQueue jobQueue = JobQueue.builder(queueDir).build();
```
where `queueDir` is `java.io.File` directory in which jobs will be stored.
After `JobQueue` construction, `queueDir` will be created (if it's not) with `initial`, `inprogress` and `done` subfolders. This folders will contains jobs with corresponding lifecycle status.

Job submission looks like
```java
jobQueue.enqueue(job);
```
Where `job` is a subtype of `AbstractJob`, `RetryJob` or `RetryWithDelayJob`.
```java
class NopJob extends AbstractJob {

    @Override
    public void execute() {
        // nop
    }
}

class RetryNopJob extends RetryJob {

    public NopRetryJob(attemptsCount) {
        super(attemptsCount);
    }

    @Override
    public void execute() {
        // nop
    }
}

class RetryWithDelayNopJob extends RetryWithDelayJob {

    // delay is duration between attempts
    public NopRetryWithDelayJob(Duration delay, int attemptCount) {
        super(delay, attemptCount);
    }

    @Override
    public void execute() {
        // nop
    }
}
```
You can add custom fields to your Job implementation. Serialization is implemented with [Jackson](https://github.com/FasterXML/jackson).

Also, `JobQueue` implements `getInitial()`, `getInProgress()`, `getDone()`, `getFailed()` and `getAll()` methods, which return list of jobs.

### Another configuration parameters
- `sync()` for synchronous `JobQueue` creation (for tests, or if you have some synchronous tasks, but you don't want to break the base concept of the system!)

```java
JobQueue jobQueue = JobQueue.builder(queueDir)
        .sync()
        .build();
```
- `poolSize()` to configure internal thread pool size

```java
JobQueue jobQueue = JobQueue.builder(queueDir)
        .poolSize(200)
        .build();
```
- `withJobsGc` to enable garbage collection for the old jobs

```java
Duration expirationDuration = Duration.standardHours(1);
Duration delay = Duration.standardSeconds(10);

JobQueue jobQueue = JobQueue.builder(queueDir)
        .withJobsGc(new JobsGcConfig(expirationDuration, delay))
        .build();
```
where `expirationDuration` is the duration before job expiration and `delay` is the period between successive execution.
And all together (of course, you can't set `sync()` and `poolSize()` for the same `JobQueue` :) )
```java
JobQueue jobQueue = JobQueue.builder(queueDir)
        .poolSize(200)
        .withJobsGc(new JobsGcConfig(expirationDuration, delay))
        .build();
```

Performance
-----------
For performance testing I used [jLoad](https://github.com/dreambrother/jload). 
All tests was executed on my MacBook Pro ME662 i5-2.6(3.2)GHz/8GB/256GB SSD.
Result is a spent time for one operation.

### Sync JobQueue
Tested with 100 000 jobs

Writing threads | 1 | 4 | 16 
--- | --- | --- | --- 
--- | 0,64394 ms | 0,43676 ms | 0,40101 ms

Current version
---------------
[1.1](https://bintray.com/dreambrother/maven/public/1.1?sort=&order=)

Maven
-----
```xml
<repositories>
    <repository>
        <id>central</id>
        <name>bintray</name>
        <url>http://dl.bintray.com/dreambrother/maven</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.dreambrother</groupId>
        <artifactId>jpjq</artifactId>
        <version>1.1</version>
    </dependency>
</dependencies>
```

License
-------
[Apache License, Version 2.0](https://raw.github.com/dreambrother/jpjq/master/LICENSE)
