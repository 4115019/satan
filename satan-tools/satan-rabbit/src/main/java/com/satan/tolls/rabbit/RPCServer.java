package com.satan.tolls.rabbit;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by huangpin on 17/6/16.
 */
public class RPCServer {

    private static final String RPC_QUEUE_NAME = "rpc_queue2";

    private static int fib(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fib(n - 1) + fib(n - 2);
    }
}
