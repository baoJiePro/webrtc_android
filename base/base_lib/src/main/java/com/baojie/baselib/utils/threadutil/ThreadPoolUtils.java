package com.baojie.baselib.utils.threadutil;

import static com.baojie.baselib.utils.threadutil.ThreadConstants.TYPE_CACHED;
import static com.baojie.baselib.utils.threadutil.ThreadConstants.TYPE_CPU;
import static com.baojie.baselib.utils.threadutil.ThreadConstants.TYPE_IO;
import static com.baojie.baselib.utils.threadutil.ThreadConstants.TYPE_SCHEDULE;
import static com.baojie.baselib.utils.threadutil.ThreadConstants.TYPE_SCHEDULE_ONE;
import static com.baojie.baselib.utils.threadutil.ThreadConstants.TYPE_SINGLE;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 线程池封装库
 * @Author baoJie
 * @Date 2022/4/28 16:38
 */
public class ThreadPoolUtils {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private static final Map<Integer, Map<Integer, ExecutorService>> TYPE_PRIORITY_POOLS =
            new HashMap<>();

    public static final Map<Runnable, ExecutorService> TASK_POOL_MAP = new ConcurrentHashMap<>();

    private static Executor sDeliver;

    /**
     * Return whether the thread is the main thread.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static Handler getMainHandler() {
        return HANDLER;
    }

    public static void runOnUiThread(final Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            HANDLER.post(runnable);
        }
    }

    public static void runOnUiThreadDelayed(final Runnable runnable, long delayMillis) {
        HANDLER.postDelayed(runnable, delayMillis);
    }

    /**
     * Return a thread pool that reuses a fixed number of threads
     * operating off a shared unbounded queue, using the provided
     * ThreadFactory to create new threads when needed.
     *
     * @param size The size of thread in the pool.
     * @return a fixed thread pool
     */
    public static ExecutorService getFixedPool(@IntRange(from = 1) final int size) {
        return getPoolByTypeAndPriority(size);
    }

    /**
     * Return a thread pool that reuses a fixed number of threads
     * operating off a shared unbounded queue, using the provided
     * ThreadFactory to create new threads when needed.
     *
     * @param size     The size of thread in the pool.
     * @param priority The priority of thread in the poll.
     * @return a fixed thread pool
     */
    public static ExecutorService getFixedPool(@IntRange(from = 1) final int size,
                                               @IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(size, priority);
    }

    /**
     * Return a thread pool that uses a single worker thread operating
     * off an unbounded queue, and uses the provided ThreadFactory to
     * create a new thread when needed.
     *
     * @return a single thread pool
     */
    public static ExecutorService getSinglePool() {
        return getPoolByTypeAndPriority(TYPE_SINGLE);
    }

    /**
     * Return a thread pool that uses a single worker thread operating
     * off an unbounded queue, and uses the provided ThreadFactory to
     * create a new thread when needed.
     *
     * @param priority The priority of thread in the poll.
     * @return a single thread pool
     */
    public static ExecutorService getSinglePool(@IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_SINGLE, priority);
    }

    /**
     * Return a thread pool that creates new threads as needed, but
     * will reuse previously constructed threads when they are
     * available.
     *
     * @return a cached thread pool
     */
    public static ExecutorService getCachedPool() {
        return getPoolByTypeAndPriority(TYPE_CACHED);
    }

    /**
     * Return a thread pool that creates new threads as needed, but
     * will reuse previously constructed threads when they are
     * available.
     *
     * @param priority The priority of thread in the poll.
     * @return a cached thread pool
     */
    public static ExecutorService getCachedPool(@IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_CACHED, priority);
    }

    /**
     * Return a thread pool that creates (2 * CPU_COUNT + 1) threads
     * operating off a queue which size is 128.
     *
     * @return a IO thread pool
     */
    public static ExecutorService getIoPool() {
        return getPoolByTypeAndPriority(TYPE_IO);
    }

    /**
     * Return a thread pool that creates (2 * CPU_COUNT + 1) threads
     * operating off a queue which size is 128.
     *
     * @param priority The priority of thread in the poll.
     * @return a IO thread pool
     */
    public static ExecutorService getIoPool(@IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_IO, priority);
    }

    /**
     * Return a thread pool that creates (CPU_COUNT + 1) threads
     * operating off a queue which size is 128 and the maximum
     * number of threads equals (2 * CPU_COUNT + 1).
     *
     * @return a cpu thread pool for
     */
    public static ExecutorService getCpuPool() {
        return getPoolByTypeAndPriority(TYPE_CPU);
    }

    /**
     * Return a thread pool that creates (CPU_COUNT + 1) threads
     * operating off a queue which size is 128 and the maximum
     * number of threads equals (2 * CPU_COUNT + 1).
     *
     * @param priority The priority of thread in the poll.
     * @return a cpu thread pool for
     */
    public static ExecutorService getCpuPool(@IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_CPU, priority);
    }

    public static ExecutorService getSchedulePool(){
        return getPoolByTypeAndPriority(TYPE_SCHEDULE);
    }

    public static ExecutorService getScheduleSinglePool(){
        return getPoolByTypeAndPriority(TYPE_SCHEDULE_ONE);
    }

    private static ExecutorService getPoolByTypeAndPriority(final int type) {
        return getPoolByTypeAndPriority(type, Thread.NORM_PRIORITY);
    }

    private static ExecutorService getPoolByTypeAndPriority(final int type, final int priority) {
        synchronized (TYPE_PRIORITY_POOLS) {
            ExecutorService pool;
            Map<Integer, ExecutorService> priorityPools = TYPE_PRIORITY_POOLS.get(type);
            if (priorityPools == null) {
                priorityPools = new ConcurrentHashMap<>();
                pool = ThreadPoolExecuteCreateFactory.createPool(type, priority);
                priorityPools.put(priority, pool);
                TYPE_PRIORITY_POOLS.put(type, priorityPools);
            } else {
                pool = priorityPools.get(priority);
                if (pool == null) {
                    pool = ThreadPoolExecuteCreateFactory.createPool(type, priority);
                    priorityPools.put(priority, pool);
                }
            }
            return pool;
        }
    }

    /**
     * Executes the given task in a fixed thread pool.
     *
     * @param size The size of thread in the fixed thread pool.
     * @param task The task to execute.
     */
    public static void executeByFixed(@IntRange(from = 1) final int size,
                                      final Runnable task) {
        execute(getPoolByTypeAndPriority(size), task);
    }

    /**
     * Executes the given task in a fixed thread pool.
     *
     * @param size     The size of thread in the fixed thread pool.
     * @param task     The task to execute.
     * @param priority The priority of thread in the poll.
     */
    public static void executeByFixed(@IntRange(from = 1) final int size,
                                      final Runnable task,
                                      @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(size, priority), task);
    }

    /**
     * Executes the given task in a fixed thread pool after the given delay.
     *
     * @param task  The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit  The time unit of the delay parameter.
     */
    public static void schedule(
            final Runnable task,
            final long delay,
            final TimeUnit unit) {
        schedule(task, delay, unit, false);
    }

    /**
     * Executes the given task in a fixed thread pool after the given delay.
     *
     * @param task       The task to execute.
     * @param delay      The time from now to delay execution.
     * @param unit       The time unit of the delay parameter.
     * @param singlePlan single plan
     */
    public static <T> void schedule(
            final Runnable task,
            final long delay,
            final TimeUnit unit, final boolean singlePlan) {
        if (singlePlan) {
            ((ScheduledPoolExecutorWithRemove) getPoolByTypeAndPriority(TYPE_SCHEDULE_ONE)).schedule(task,
                    delay, unit);
        } else {
            ((ScheduledPoolExecutorWithRemove) getPoolByTypeAndPriority(TYPE_SCHEDULE)).schedule(task,
                    delay, unit);
        }

    }

    /**
     * Executes the given task in a fixed thread pool after the given delay.
     *
     * @param task         The task to execute.
     * @param initialDelay delay time.
     * @param delay        The time from now to delay execution.
     * @param unit         The time unit of the delay parameter.
     */
    public static void scheduleAtFixedRate(
            final Runnable task,
            final long initialDelay,
            final long delay,
            final TimeUnit unit) {
        scheduleAtFixedRate(task, initialDelay, delay, unit, false);
    }


    /**
     * Executes the given task in a fixed thread pool after the given delay.
     *
     * @param task         The task to execute.
     * @param initialDelay delay time.
     * @param delay        The time from now to delay execution.
     * @param unit         The time unit of the delay parameter.
     * @param singlePlan   single plan
     */
    public static void scheduleAtFixedRate(
            final Runnable task,
            final long initialDelay,
            final long delay,
            final TimeUnit unit, boolean singlePlan) {
        if (singlePlan) {
            ((ScheduledPoolExecutorWithRemove) getPoolByTypeAndPriority(TYPE_SCHEDULE_ONE)).scheduleAtFixedRate(task, initialDelay,
                    delay, unit);
        } else {
            ((ScheduledPoolExecutorWithRemove) getPoolByTypeAndPriority(TYPE_SCHEDULE)).scheduleAtFixedRate(task, initialDelay,
                    delay, unit);
        }

    }

    /**
     * Executes the given task in a fixed thread pool after the given delay.
     *
     * @param task         The task to execute.
     * @param initialDelay delay time.
     * @param delay        The time from now to delay execution.
     * @param unit         The time unit of the delay parameter.
     */
    public static void scheduleWithFixedDelay(
            final Runnable task,
            final long initialDelay,
            final long delay,
            final TimeUnit unit) {
        scheduleWithFixedDelay(task, initialDelay, delay, unit, false);
    }

    /**
     * Executes the given task in a fixed thread pool after the given delay.
     *
     * @param task         The task to execute.
     * @param initialDelay delay time.
     * @param delay        The time from now to delay execution.
     * @param unit         The time unit of the delay parameter.
     * @param singlePlan   singlePlan
     */
    public static void scheduleWithFixedDelay(
            final Runnable task,
            final long initialDelay,
            final long delay,
            final TimeUnit unit, boolean singlePlan) {
        if (singlePlan) {
            ((ScheduledPoolExecutorWithRemove) getPoolByTypeAndPriority(TYPE_SCHEDULE_ONE)).scheduleWithFixedDelay(task, initialDelay,
                    delay, unit);
        } else {
            ((ScheduledPoolExecutorWithRemove) getPoolByTypeAndPriority(TYPE_SCHEDULE)).scheduleWithFixedDelay(task, initialDelay,
                    delay, unit);
        }
    }

    private static void execute(final ExecutorService pool, final Runnable task) {
        synchronized (TASK_POOL_MAP) {
            if (TASK_POOL_MAP.get(task) != null) {
                Log.e("ThreadUtils", "Task can only be executed once.");
                return;
            }
            TASK_POOL_MAP.put(task, pool);
        }
        pool.execute(task);
    }

    public static void executeBySingle(final Runnable task) {
        execute(getPoolByTypeAndPriority(TYPE_SINGLE), task);
    }

    public static void executeBySingle(final Runnable task,
                                       @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_SINGLE, priority), task);
    }

    public static void executeByCached(final Runnable task) {
        execute(getPoolByTypeAndPriority(TYPE_CACHED), task);
    }

    public static void executeByCached(final Runnable task,
                                       @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_CACHED, priority), task);
    }

    public static void executeByIo(final Runnable task) {
        execute(getPoolByTypeAndPriority(TYPE_IO), task);
    }

    public static void executeByIo(final Runnable task,
                                   @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_IO, priority), task);
    }

    public static void executeByCpu(final Runnable task) {
        execute(getPoolByTypeAndPriority(TYPE_CPU), task);
    }

    public static void executeByCpu(final Runnable task,
                                    @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_CPU, priority), task);
    }

    public static void executeByCustom(final ExecutorService pool, final Runnable task) {
        execute(pool, task);
    }

    /**
     * Cancel the given task.
     *
     * @param runnable The task to cancel.
     */
    public static void cancel(final CancelRunnable runnable) {
        if (runnable == null) return;
        runnable.cancel();
        if (runnable.isSchedule()){
            ((ScheduledPoolExecutorWithRemove)getScheduleSinglePool()).remove(runnable);
            ((ScheduledPoolExecutorWithRemove)getSchedulePool()).remove(runnable);
        }
    }

    /**
     * Cancel the given tasks.
     *
     * @param runnables The tasks to cancel.
     */
    public static void cancel(final CancelRunnable... runnables) {
        if (runnables == null || runnables.length == 0) return;
        for (CancelRunnable task : runnables) {
            if (task == null) continue;
            if (task.isSchedule()){
                ((ScheduledPoolExecutorWithRemove)getScheduleSinglePool()).remove(task);
                ((ScheduledPoolExecutorWithRemove)getSchedulePool()).remove(task);
            }
            task.cancel();
        }
    }

    /**
     * Cancel the given tasks.
     *
     * @param tasks The tasks to cancel.
     */
    public static void cancel(final List<CancelRunnable> tasks) {
        if (tasks == null || tasks.size() == 0) return;
        for (CancelRunnable task : tasks) {
            if (task == null) continue;
            if (task.isSchedule()){
                ((ScheduledPoolExecutorWithRemove)getScheduleSinglePool()).remove(task);
                ((ScheduledPoolExecutorWithRemove)getSchedulePool()).remove(task);
            }
            task.cancel();
        }
    }

    public static Executor getGlobalDeliver() {
        if (sDeliver == null) {
            sDeliver = new Executor() {
                @Override
                public void execute(@NonNull Runnable command) {
                    runOnUiThread(command);
                }
            };
        }
        return sDeliver;
    }

}
