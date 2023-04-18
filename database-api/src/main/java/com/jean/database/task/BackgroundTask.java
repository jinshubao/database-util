package com.jean.database.task;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.UUID;

public abstract class BackgroundTask<V> extends Task<V> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String taskId;

    private final String name;

    public BackgroundTask(String taskName) {
        this(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()), taskName);
    }

    public BackgroundTask(String taskId, String taskName) {
        this.name = taskName;
        this.taskId = taskId;
    }

    @Override
    protected final V call() throws Exception {
        logger.debug("task[{}] call", this);
        try {
            return doBackground();
        } catch (Throwable e) {
            throw new Exception(e);
        }
    }

    @Override
    public boolean cancel(boolean b) {
        logger.debug("task[{}] cancel", this);
        return super.cancel(b);
    }

    protected abstract V doBackground() throws Exception;

    @Override
    protected void running() {
        super.running();
        logger.debug("task[{}] running", this);
    }

    @Override
    protected void scheduled() {
        logger.debug("task[{}] scheduled", this);
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        logger.debug("task[{}] cancelled", this);
    }

    @Override
    protected void failed() {
        super.failed();
        logger.error("task[{}] failed {}", this, getException().getMessage());
    }

    @Override
    protected void succeeded() {
        super.done();
        logger.debug("task[{}] succeeded", this);
    }

    @Override
    protected void done() {
        super.done();
        logger.debug("task[{}] done", this);
    }

    @Override
    public String toString() {
        return taskId + ": " + name;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
    }

}
