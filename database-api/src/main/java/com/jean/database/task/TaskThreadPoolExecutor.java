package com.jean.database.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class TaskThreadPoolExecutor extends ThreadPoolExecutor implements RejectedExecutionHandler {
    private static final Logger logger = LoggerFactory.getLogger(TaskThreadPoolExecutor.class);

    public TaskThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        setRejectedExecutionHandler(this);
    }

    @Override
    public void execute(Runnable command) {
        super.execute(command);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        logger.debug("任务[{}]开始执行", r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        logger.debug("任务[{}]执行完成", r);
        if (t != null) {
            logger.error("execute task {} has an exception {}", r, t);
            throw new RuntimeException(t);
        }
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        logger.error("任务[{}]被拒绝", r);
        throw new RejectedExecutionException("线程池已满");
    }
}
