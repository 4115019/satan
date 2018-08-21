package acm;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by h
 * on 2018-07-25 14:50.
 *
 * @author h
 */
@Slf4j
public class RectCover {

    public int rectCover(int target) {
        if (target <= 0){
            return 0;
        }
        if (target == 1) {
            return 1;
        }

        if (target == 2) {
            return 2;
        }

        int a = 1;
        int b = 2;
        int temp = 0;
        for (int i = 3; i <= target; i++) {
            temp = a + b;
            a = b;
            b = temp;
        }

        return b;
    }
}