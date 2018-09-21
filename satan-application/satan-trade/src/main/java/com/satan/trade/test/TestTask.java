package com.satan.trade.test;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Created by h
 * on 2018-09-19 09:48.
 *
 * @author h
 */
@Slf4j
public class TestTask implements Callable<Boolean> {

    private Integer threadNum;

    public TestTask(Integer threadNum) {
        this.threadNum = threadNum;
    }

    @Override
    public Boolean call() throws InterruptedException {
        Random random = new Random();
        System.out.println("线程开始" + threadNum);
        Thread.sleep((random.nextInt(10)+1) * 1000);
        System.out.println("线程开始" + threadNum);
        return true;
    }
}