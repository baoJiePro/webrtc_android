package com.baojie.baselib.utils.threadutil;

import static com.baojie.baselib.utils.threadutil.ThreadConstants.TYPE_CACHED;
import static com.baojie.baselib.utils.threadutil.ThreadConstants.TYPE_CPU;
import static com.baojie.baselib.utils.threadutil.ThreadConstants.TYPE_IO;
import static com.baojie.baselib.utils.threadutil.ThreadConstants.TYPE_SCHEDULE;
import static com.baojie.baselib.utils.threadutil.ThreadConstants.TYPE_SCHEDULE_ONE;
import static com.baojie.baselib.utils.threadutil.ThreadConstants.TYPE_SINGLE;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description:
 * @Author baoJie
 * @Date 2022/4/28 17:12
 */
class ThreadPoolExecuteCreateFactory extends ThreadPoolExecutor {

    private static final String TAG = "ThreadPoolExecuteCreateFactory";


    private static final int  CPU_COUNT = Runtime.getRuntime().availableProcessors();

    static ExecutorService createPool(final int type, final int priority) {
        switch (type) {
            case TYPE_SINGLE:
                return new ThreadPoolExecuteCreateFactory(1, 1,
                        0L, TimeUnit.MILLISECONDS,
                        new WorkLinkedBlockingQueue(),
                        new WorkThreadFactory("single", priority)
                );
            case TYPE_CACHED:
                return new ThreadPoolExecuteCreateFactory(0, 128,
                        60L, TimeUnit.SECONDS,
                        new WorkLinkedBlockingQueue(true),
                        new WorkThreadFactory("cached", priority)
                );
            case TYPE_IO:
                return new ThreadPoolExecuteCreateFactory(2 * CPU_COUNT + 1, 2 * CPU_COUNT + 1,
                        30, TimeUnit.SECONDS,
                        new WorkLinkedBlockingQueue(),
                        new WorkThreadFactory("io", priority)
                );
            case TYPE_CPU:
                return new ThreadPoolExecuteCreateFactory(CPU_COUNT + 1, 2 * CPU_COUNT + 1,
                        30, TimeUnit.SECONDS,
                        new WorkLinkedBlockingQueue(true),
                        new WorkThreadFactory("cpu", priority)
                );
            case TYPE_SCHEDULE:
                return new ScheduledPoolExecutorWithRemove(CPU_COUNT + 1, new WorkThreadFactory("schedule", priority));
            case TYPE_SCHEDULE_ONE:
                return new ScheduledPoolExecutorWithRemove(1, new WorkThreadFactory("schedule-one", priority));
            default:
                return new ThreadPoolExecuteCreateFactory(type, type,
                        0L, TimeUnit.MILLISECONDS,
                        new WorkLinkedBlockingQueue(),
                        new WorkThreadFactory("fixed(" + type + ")", priority)
                );
        }
    }

    private final AtomicInteger mSubmittedCount = new AtomicInteger();

    private final WorkLinkedBlockingQueue mWorkQueue;

    ThreadPoolExecuteCreateFactory(int corePoolSize, int maximumPoolSize,
                            long keepAliveTime, TimeUnit unit,
                            WorkLinkedBlockingQueue workQueue,
                            ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize,
                keepAliveTime, unit,
                workQueue,
                threadFactory
        );
        workQueue.mPool = this;
        mWorkQueue = workQueue;
    }

    private int getSubmittedCount() {
        return mSubmittedCount.get();
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        mSubmittedCount.decrementAndGet();
        super.afterExecute(r, t);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void execute(@NonNull Runnable command) {
        if (this.isShutdown()) return;
        mSubmittedCount.incrementAndGet();
        try {
            super.execute(command);
        } catch (RejectedExecutionException ignore) {
            Log.e(TAG, "This will not happen!");
            mWorkQueue.offer(command);
        } catch (Throwable t) {
            mSubmittedCount.decrementAndGet();
        }
    }



    private static class WorkLinkedBlockingQueue extends LinkedBlockingQueue<Runnable>{
        private volatile ThreadPoolExecuteCreateFactory mPool;

        private int mCapacity = Integer.MAX_VALUE;

        WorkLinkedBlockingQueue() {
            super();
        }

        WorkLinkedBlockingQueue(boolean isAddSubThreadFirstThenAddQueue) {
            super();
            if (isAddSubThreadFirstThenAddQueue) {
                mCapacity = 0;
            }
        }

        WorkLinkedBlockingQueue(int capacity) {
            super();
            mCapacity = capacity;
        }

        @Override
        public boolean offer(@NonNull Runnable runnable) {
            if (mCapacity <= size() &&
                    mPool != null && mPool.getPoolSize() < mPool.getMaximumPoolSize()) {
                // create a non-core thread
                return false;
            }
            return super.offer(runnable);
        }
    }

    static final class WorkThreadFactory extends AtomicLong
            implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER      = new AtomicInteger(1);
        private final        String        namePrefix;
        private final        int           priority;
        private final        boolean       isDaemon;

        WorkThreadFactory(String prefix, int priority) {
            this(prefix, priority, false);
        }

        WorkThreadFactory(String prefix, int priority, boolean isDaemon) {
            namePrefix = prefix + "-pool-" +
                    POOL_NUMBER.getAndIncrement() +
                    "-thread-";
            this.priority = priority;
            this.isDaemon = isDaemon;
        }

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread t = new Thread(r, namePrefix + getAndIncrement()) {
                @SuppressLint("LongLogTag")
                @Override
                public void run() {
                    try {
                        super.run();
                    } catch (Throwable t) {
                        Log.e(TAG, "Request threw uncaught throwable", t);
                    }
                }
            };
            t.setDaemon(isDaemon);
            t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    System.out.println(e);
                }
            });
            t.setPriority(priority);
            return t;
        }
    }
}
