package com.apighost.loadtest.lifecycle;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.Queue;

/**
 * Manages the lifecycle of virtual user (VU) tasks during a load test.
 * <p>
 * This class tracks running {@link Future} tasks and provides mechanisms to add, cancel, and count
 * active tasks. It ensures thread-safe operations using a {@link ConcurrentLinkedQueue}, suitable
 * for concurrent environments.
 * </p>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class VuLifecycleManager {

    /**
     * A thread-safe queue that holds currently running VU tasks.
     */
    private final Queue<Future<?>> runningTasks = new ConcurrentLinkedQueue<>();

    /**
     * Adds a running virtual user task to the lifecycle manager.
     *
     * @param future the {@link Future} representing the running task
     */
    public void add(Future<?> future) {
        runningTasks.add(future);
    }

    /**
     * Cancels up to the specified number of running tasks.
     *
     * <p>
     * Tasks are cancelled in FIFO order. If there are fewer than {@code count} tasks in the queue,
     * all available tasks will be cancelled.
     * </p>
     *
     * @param count the maximum number of tasks to cancel
     * @return the actual number of tasks cancelled
     */
    public int cancel(int count) {
        int cancelled = 0;
        while (cancelled < count) {
            Future<?> future = runningTasks.poll(); // FIFO
            if (future == null) {
                break;
            }
            future.cancel(true);
            cancelled++;
        }
        return cancelled;
    }

    /**
     * Cancels all currently running virtual user tasks.
     */
    public void cancelAll() {
        Future<?> future;
        while ((future = runningTasks.poll()) != null) {
            future.cancel(true);
        }
    }

    /**
     * Returns the number of currently running virtual user tasks.
     *
     * @return the number of active tasks in the queue
     */
    public int getRunningCount() {
        return runningTasks.size();
    }
}
