package com.satan.tolls.rabbit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by huangpin on 17/6/16.
 */
public class RPCRabbitTest {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        RPCClient fibonacciRpc = new RPCClient();

        System.out.println(" [x] Requesting fib(30)");
        String response = fibonacciRpc.call("30","rpc_queue1");
        String response1 = fibonacciRpc.call("15","rpc_queue2");
        System.out.println(" [.] Got '" + response + "'");
        System.out.println(" [.] Got '" + response1 + "'");

        fibonacciRpc.close();
    }
}
