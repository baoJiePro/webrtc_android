package com.baojie.baselib.utils.threadutil;

import com.blankj.utilcode.util.LogUtils;

import java.util.HashMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author baoJie
 * @Date 2022/4/29 15:54
 */
class ScheduledPoolExecutorWithRemove extends ScheduledThreadPoolExecutor{

   private HashMap<Runnable, RunnableScheduledFuture> map = new HashMap<>();
   public ScheduledPoolExecutorWithRemove(int corePoolSize) {
      super(corePoolSize);
      setRemoveOnCancelPolicy(true);
   }

   public ScheduledPoolExecutorWithRemove(int corePoolSize, ThreadFactory threadFactory) {
      super(corePoolSize, threadFactory);
      setRemoveOnCancelPolicy(true);
   }

   public ScheduledPoolExecutorWithRemove(int corePoolSize, RejectedExecutionHandler handler) {
      super(corePoolSize, handler);
      setRemoveOnCancelPolicy(true);
   }

   public ScheduledPoolExecutorWithRemove(int corePoolSize, ThreadFactory threadFactory,
                                          RejectedExecutionHandler handler) {
      super(corePoolSize, threadFactory, handler);
      setRemoveOnCancelPolicy(true);
   }

   @Override
   public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
      RunnableScheduledFuture<?> scheduledFuture = (RunnableScheduledFuture<?>) super.schedule(command, delay, unit);
      map.put(command, scheduledFuture);
      return scheduledFuture;
   }

   @Override
   public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period
           , TimeUnit unit) {
      RunnableScheduledFuture<?> scheduledFuture = (RunnableScheduledFuture<?>) super.scheduleAtFixedRate(command, initialDelay, period, unit);
      map.put(command, scheduledFuture);
      return scheduledFuture;
   }

   @Override
   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay,
                                                    long delay, TimeUnit unit) {
      RunnableScheduledFuture<?> scheduledFuture = (RunnableScheduledFuture<?>) super.scheduleWithFixedDelay(command, initialDelay, delay, unit);
      map.put(command, scheduledFuture);
      return scheduledFuture;
   }

   @Override
   public boolean remove(Runnable task) {
      RunnableScheduledFuture<?> future = map.get(task);
      if (future != null){
         LogUtils.d("anjing,", future.toString());
         return super.remove(future);
      }
      return false;
   }
}
