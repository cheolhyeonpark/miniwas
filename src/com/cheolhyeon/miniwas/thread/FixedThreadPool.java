package com.cheolhyeon.miniwas.thread;

import java.util.LinkedList;
import java.util.Queue;

public class FixedThreadPool {

    private final int numberOfThread;
    private final Queue<Runnable> taskQueue;
    private final ThreadExcecutor[] threads;

    public FixedThreadPool(int numberOfThread) {
        this.numberOfThread = numberOfThread;
        taskQueue = new LinkedList<Runnable>();
        threads = new ThreadExcecutor[numberOfThread];
        init();
    }

    private void init() {
        for (int i = 0; i < numberOfThread; i++) {
            threads[i] = new ThreadExcecutor(taskQueue);
            threads[i].start();
        }
    }

    public void execute(Runnable task) {
        synchronized (taskQueue) {
            taskQueue.offer(task);
            taskQueue.notifyAll();
        }
    }
}
