package com.imfan.j.a91fan.common.infra;

import java.util.concurrent.Executor;

/**
 * Created by jay on 17-1-14.
 */

public abstract class AbstractTaskWorker {
    /**
     * execute callback
     */
    private ExecuteCallback executeCallback;

    public AbstractTaskWorker() {

    }

    /**
     * dispatch
     * @param task
     * @return Executor
     */
    protected abstract Executor getTaskHost(Task task);

    public void setExecuteCallback(ExecuteCallback executeCallback) {
        this.executeCallback = executeCallback;
    }

    public void execute(Task task) {
        getExecutor(task).execute(getRunnable(task));
    }

    private final Executor getExecutor(Task task) {
        if (task.info.background) {
            Executor executor = getTaskHost(task);
            if (executor != null) {
                return executor;
            }
        }

        return com.imfan.j.a91fan.common.infra.TaskExecutor.IMMEDIATE_EXECUTOR;
    }

    private final Runnable getRunnable(final com.imfan.j.a91fan.common.infra.Task task) {
        return new Runnable() {
            @Override
            public void run() {
                // schedule
                boolean unschedule = task.schedule();

                // callback
                if (executeCallback != null) {
                    executeCallback.onExecuted(task, unschedule);
                }
            }
        };
    }

    public interface ExecuteCallback {
        public void onExecuted(Task task, boolean unschedule);
    }
}
