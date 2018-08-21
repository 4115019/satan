package acm;

import lombok.extern.slf4j.Slf4j;

/**
 * 斐波那契数列
 * <p>
 * Created by h
 * on 2018-07-24 14:40.
 *
 * @author h
 */
@Slf4j
public class FibonacciArray {
    /**
     * 0 1 1 2 3 5
     *
     * @param n
     * @return
     */
    public int Fibonacci(int n) {
        if (n == 0) {
            return 0;
        }

        if (n == 1) {
            return 1;
        }

        int temp = 0;
        int temp1 = 1;
        for (int i = 0; i < n / 2; i++) {
            temp += temp1;
            temp1 += temp;
        }

        return n % 2 == 1 ? temp1 : temp;
    }
}