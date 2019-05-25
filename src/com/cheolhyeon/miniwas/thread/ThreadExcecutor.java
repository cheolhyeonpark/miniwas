package com.cheolhyeon.miniwas.thread;

import java.util.Queue;

public class ThreadExcecutor extends Thread {

    final private Queue<Runnable> taskQueue;

    public ThreadExcecutor(Queue<Runnable> taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (true) {
            Runnable task = null;
            synchronized (taskQueue) {
                while (taskQueue.isEmpty()) {
                    try {
                        taskQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                task = taskQueue.poll();
            }
            try {
                task.run();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }
}
