package com.baojie.baselib.utils.threadutil;

import com.blankj.utilcode.util.LogUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @Author baoJie
 * @Date 2022/4/29 13:48
 */
public abstract class CancelRunnable implements Runnable {

   private static final int NEW         = 0;
   private static final int RUNNING     = 1;
   private static final int EXCEPTIONAL = 2;
   private static final int COMPLETING  = 3;
   private static final int CANCELLED   = 4;

   private final AtomicInteger state = new AtomicInteger(NEW);

   private volatile Thread  runner;

   protected boolean isSchedule(){
      return false;
   }

   protected void onCancel(){

   }

   protected void onSuccess(){

   }

   protected void onFail(Throwable throwable){

   }

   //做任务
   protected abstract void doWork();

   @Override
   public void run() {
      if (isSchedule()){
         if (runner == null){
            if (!state.compareAndSet(NEW, RUNNING)) return;
            runner = Thread.currentThread();
         }else {
            if (isCanceled()){
               throw new RuntimeException();
            }
            if (state.get() != RUNNING) return;
         }
      }else {
         if (!state.compareAndSet(NEW, RUNNING)) return;
         runner = Thread.currentThread();
      }
      try{
         if (runner.isInterrupted()){
            return;
         }
         doWork();
         if (!state.compareAndSet(RUNNING, COMPLETING)) return;
         getDeliver().execute(new Runnable() {
            @Override
            public void run() {
               onSuccess();
               onDone();
               if (isSchedule()){
                  state.set(RUNNING);
               }
            }
         });
      } catch (Throwable throwable){
         if (!state.compareAndSet(RUNNING, EXCEPTIONAL)) return;
         getDeliver().execute(new Runnable() {
            @Override
            public void run() {
               onFail(throwable);
               onDone();
            }
         });
      }
   }

   public void cancel() {
      cancel(true);
   }

   public boolean isCanceled() {
      return state.get() >= CANCELLED;
   }

   public boolean isDone() {
      return state.get() > RUNNING;
   }

   public void cancel(boolean mayInterruptIfRunning) {
      synchronized (state) {
         if (state.get() > RUNNING) return;
         state.set(CANCELLED);
      }
      if (mayInterruptIfRunning) {
         if (runner != null) {
            runner.interrupt();
         }
      }

      getDeliver().execute(new Runnable() {
         @Override
         public void run() {
            onCancel();
            onDone();
         }
      });
   }

   protected void onDone() {
      ThreadPoolUtils.TASK_POOL_MAP.remove(this);
   }

   private Executor getDeliver() {
      return ThreadPoolUtils.getGlobalDeliver();
   }
}
