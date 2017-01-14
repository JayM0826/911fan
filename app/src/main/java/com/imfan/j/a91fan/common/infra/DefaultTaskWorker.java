package com.imfan.j.a91fan.common.infra;

/**
 * Created by jay on 17-1-14.
 */

public class DefaultTaskWorker extends TaskWorker {
    public DefaultTaskWorker() {
        this("Default");
    }

    public DefaultTaskWorker(String name) {
        this(name, TaskExecutor.defaultConfig);
    }

    public DefaultTaskWorker(String name, TaskExecutor.Config config) {
        super(new TaskExecutor("TW#" + name, config));
    }
}
