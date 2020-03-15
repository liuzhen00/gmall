package com.zl.gmall.items.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * @author shkstart
 * @create 2020-03-04 14:54
 */
public class CompletableFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /*
           创建异步对象的四种方式：
               static CompletableFuture<Void> runAsync(Runnable runnable)
               public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor)
                public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
                public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor)

                runAsync:方法不支持返回值
                supplyAsync:方法支持返回值

               方法计算完成时 的回调方法
               whenComplete()
               whenCompleteAsync():
                         可以处理正常的或者是异常的计算结果
                         区别：
                          前者：把继续执行当前线程的任务，后置是把当前任务交给线程池来之执行
                       方法的特点：不以Async结尾的将的，则使用相同的Action继续执行，如果以它开头，可能会将任务交给其它线程执行
                                 但是也有可能将最后执行的任务时同一线程来执行的。
               exceptionally()
                          处理异常的计算结果。
               handle:是对执行任务完成时，对结果集的处理。
               public <U> CompletionStage<U> handle(BiFunction<? super T, Throwable, ? extends U> fn);
                public <U> CompletionStage<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn);
                public <U> CompletionStage<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn,Executor executor);

                //串行化方法：
                thenApply:当一个线程依赖另一个线程时，获取上一个任务结果
                thenAccept: 消费型处理结果，消费处理，无返回结果
                thenRun:只要上面的任务执行完成，就开始执行thenRun
         */

//        1、创建异步执行线程
      /*  CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {   //无参有返回值

            System.out.println(Thread.currentThread().getName() + "\tCompletableFuture");
            int i = 1 / 0;
            return 1024;
        }).thenApplyAsync((t) -> {
            System.out.println("上次一次的返回值结果:" + t);
            return t * 2;

        }).whenComplete((t, u) -> {   //属于消费型接口 有参无返回值
            System.out.println("========================第一次执行=====================");
            System.out.println("上一次执行的结果:" + t.toString());
            System.out.println("异常信息:" + u);
        }).exceptionally((t) -> { //功能型接口 有参有返回值
            System.out.println("=======================第二次执行========================");
            System.out.println("异常信息的执行:" + t.toString());
            return 66666;
        }).handle((t, u) -> {     //消费型接口
            System.out.println(t);
            return 8888;
        });

        System.out.println(future.get());*/

      CompletableFuture.supplyAsync(()->{
          return "hello";
      }).thenApplyAsync(t->{
          return t+"world";
      }).thenCombineAsync(CompletableFuture.completedFuture("CompletableFuture"),(t,u)->{
          return t+u;
      }).whenComplete((t,u)->{
          System.out.println(t);
      });

    }

}
