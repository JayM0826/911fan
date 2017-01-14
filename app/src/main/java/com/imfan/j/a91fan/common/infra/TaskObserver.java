package com.imfan.j.a91fan.common.infra;

/**
 * Created by jay on 17-1-14.
 */

public interface TaskObserver {
    /**
     * on task result
     * @param task
     * @param results
     */
    public void onTaskResult(com.imfan.j.a91fan.common.infra.Task task, Object[] results);

    /**
     * on task progress
     * @param task
     * @param params
     */
    public void onTaskProgress(com.imfan.j.a91fan.common.infra.Task task, Object[] params);
}