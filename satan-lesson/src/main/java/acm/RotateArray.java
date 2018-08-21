package acm;

import lombok.extern.slf4j.Slf4j;

/**
 * 旋转数组
 * <p>
 * Created by h
 * on 2018-07-16 17:05.
 *
 * @author h
 */
@Slf4j
public class RotateArray {
    public int minNumberInRotateArray(int[] array) {

        if (array == null || array.length == 0) {
            return 0;
        }


        int temp = array[0];

        for (int i = array.length - 1; i > 0; i--) {
            if (temp > array[i]){
                temp = array[i];
            }

            if (temp < array[i]){
                break;
            }
        }

        return temp;
    }
}