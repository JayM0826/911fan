package com.imfan.j.a91fan.common.infra;

import java.util.concurrent.Executor;

/**
 * Created by jay on 17-1-14.
 */

public class TaskWorker extends AbstractTaskWorker {
    /**
     * executor
     */
    private Executor executor;

    public TaskWorker(Executor executor) {
        this.executor = executor;
    }

    @Override
    protected Executor getTaskHost(Task task) {
        return executor;
    }
}
