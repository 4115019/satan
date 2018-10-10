package acm;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h
 * on 2018-10-09 19:21.
 *
 * @author h
 */
@Slf4j
public class ReOrder {
    public void reOrderArray(int[] array) {

        List<Integer> a = new ArrayList<>();
        List<Integer> b = new ArrayList<>();
        for (int i : array) {
            if (i % 2 == 0) {
                a.add(i);
            } else {
                b.add(i);
            }
        }


        for (int index = 0; index < b.size(); index++) {
            array[index] = b.get(index);
        }

        for (int index = 0;index < a.size();index++){
            array[index+b.size()] = a.get(index);
        }
    }

    public static void main(String[] args) {
        new ReOrder().reOrderArray(new int[]{1,2,3,4});
    }
}