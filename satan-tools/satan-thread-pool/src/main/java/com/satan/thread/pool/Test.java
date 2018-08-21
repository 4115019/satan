package com.satan.thread.pool;


import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by h
 * on 2018-08-13 12:49.
 *
 * @author h
 */
public class Test {
    private static int produceTaskSleepTime = 2;
    private static int produceTaskMaxNumber = 10;

    public static void main(String[] args) {
        // 构造一个线程池
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2),
                new ThreadPoolExecutor.DiscardOldestPolicy());

        for (int i = 1; i <= produceTaskMaxNumber; i++) {
            try {
                // 产生一个任务，并将其加入到线程池
                String task = "task@ " + i;
                System.out.println("put " + task);
                threadPool.execute(new ThreadPoolTask(task));

                // 便于观察，等待一段时间
                Thread.sleep(produceTaskSleepTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}