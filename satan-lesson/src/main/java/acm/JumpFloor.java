package acm;

import lombok.extern.slf4j.Slf4j;

/**
 * JumpFloor
 * <p>
 * Created by h
 * on 2018-07-24 14:54.
 *
 * @author h
 */
@Slf4j
public class JumpFloor {
    public static int jumpFloor(int target) {

        if (target <= 0) {
            return 0;
        }

        if (target == 1) {
            return 1;
        }

        if (target == 2) {
            return 2;
        }

        int temp = 1;
        int temp1 = 2;

        for (int i = 3; i < target / 2; i++) {
            temp +=temp1;
            temp1 += temp;
        }

        if (target % 2 == 1){
            return temp1;
        } else {
            return temp;
        }
    }

    public static void main(String[] args) {
        System.out.println(jumpFloor(2));
    }
}