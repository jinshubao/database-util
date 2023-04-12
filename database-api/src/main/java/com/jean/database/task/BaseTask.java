package com.jean.database.task;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public abstract class BaseTask<V> extends Task<V> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String taskId;

    public BaseTask() {
        this.taskId = UUID.randomUUID().toString();
    }

    @Override
    protected void running() {
        super.running();
        logger.debug("task[{}] running", this);
    }

    @Override
    protected void scheduled() {
//        updateMessage("开始执行");
        logger.debug("task[{}] scheduled", this);
    }

    @Override
    protected void cancelled() {
        super.cancelled();
//        updateMessage("执行取消");
        logger.debug("task[{}] cancelled", this);
    }

    @Override
    protected void failed() {
        super.failed();
//        updateMessage("执行失败");
        logger.debug("task[{}] failed", this);
//        Throwable exception = getException();
//        if (exception != null) {
//            DialogUtil.error(exception);
//        }
    }

    @Override
    protected void succeeded() {
        super.done();
//        updateMessage("执行成功");
        logger.debug("task[{}] succeeded", this);
    }

    @Override
    protected void done() {
        super.done();
        logger.debug("task[{}] done", this);
    }

    @Override
    public String toString() {
        return taskId;
    }

    public String getTaskId() {
        return taskId;
    }

}
