package acm;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by h
 * on 2018-08-23 14:53.
 *
 * @author h
 */
@Slf4j
public class Power {

    public static void main(String[] args) throws Exception {
        System.out.println(Power(4, 3));
    }

    public static double Power(double base, int exponent) throws Exception {
        if (exponent == 0) {
            return 1;
        }

        if (exponent < 0 && base == 0) {
            throw new Exception("分母不能为零");
        }

        if (base == 0) {
            return 0;
        }

        int n = exponent > 0 ? exponent : -exponent;
        double result = 1;
        double current = base;

        while (n != 0) {
            if ((n & 1) == 1) {
                result *= current;
            }

            current *= current;

            n = n >> 1;
        }

        return exponent > 0 ? result : (1 / result);

    }
}