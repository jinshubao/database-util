package com.jean.database.task;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class TaskBuilder<T> {

    private BackgroundTask<T> task;

    private TaskBuilder() {
    }

    public static <T> TaskBuilder<T> newBuilder() {
        return new TaskBuilder<>();
    }

    public TaskBuilder<T> task(String name, Callable<T> callable) {
        this.task = new InnerTask<>(name, callable);
        return this;
    }

    public TaskBuilder<T> onSuccess(Consumer<T> onSuccess) {
        task.setOnScheduled((event) -> onSuccess.accept(task.getValue()));
        return this;
    }

    public TaskBuilder<T> onFailed(Consumer<Throwable> onFailed) {
        task.setOnFailed((event) -> onFailed.accept(task.getException()));
        return this;
    }

    public BackgroundTask<T> build() {
        return task;
    }

    private static class InnerTask<T> extends BackgroundTask<T> {

        private final Callable<T> callable;

        public InnerTask(Callable<T> callable) {
            super("inner task");
            this.callable = callable;
        }

        public InnerTask(String name, Callable<T> callable) {
            super(name);
            this.callable = callable;
        }

        @Override
        protected T doBackground() throws Exception {
            return callable.call();
        }
    }
}
