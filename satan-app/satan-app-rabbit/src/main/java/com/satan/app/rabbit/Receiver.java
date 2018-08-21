package com.satan.app.rabbit;

import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

/**
 * rabbitReveiver
 * <p>
 * Created by h
 * on 2018-06-21 17:01.
 *
 * @author h
 */
@Service
public class Receiver {

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}