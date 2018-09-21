package com.satan.trade.test;

import com.satan.trade.bi.tian.HttpBiTianTask;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * Created by h
 * on 2018-09-19 09:51.
 *
 * @author h
 */
@Slf4j
public class TestMain {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        int i = 1;
        while (true) {
            System.out.println(i+"次开始");
            ExecutorService executorService = Executors.newCachedThreadPool();
            CompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(
                    executorService);
            completionService.submit(new TestTask(1));
            completionService.submit(new TestTask(2));
            completionService.submit(new TestTask(3));
            completionService.submit(new TestTask(4));
            for (int index = 0; index < 4; index++) {
                System.out.println(completionService.take().get());
            }

            executorService.shutdown();
            System.out.println(i++ +"次结束");

        }

    }
}