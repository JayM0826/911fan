package com.imfan.j.a91fan.common.infra;

/**
 * Created by jay on 17-1-14.
 */

public class DefaultTaskManager extends TaskManager {
    public DefaultTaskManager() {
        this(new DefaultTaskWorker());
    }

    public DefaultTaskManager(String name) {
        this(new DefaultTaskWorker(name));
    }

    public DefaultTaskManager(AbstractTaskWorker worker) {
        super(new DefaultTaskScheduler(worker));
    }
}

