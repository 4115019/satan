package acm;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by h
 * on 2018-08-23 14:39.
 *
 * @author h
 */
@Slf4j
public class QuickSort {

    public static void main(String[] args) {
        int[] data = new int[]{7,1111,23,34,546,5,10,3,1,4,5,6,67};
        sort(data,0,data.length-1);
        System.out.println(JSON.toJSONString(data));
    }

    public static void sort(int data[], int l, int r) {

        if (l < r) {
            int i = l;
            int j = r;
            int temp = data[i];

            while (i < j) {

                while (i < j && data[j] >= temp) {
                    j--;
                }
                if (i < j) {
                    data[i++] = data[j];
                }

                while (i < j && data[i] < temp) {
                    i++;
                }
                if (i < j) {
                    data[j--] = data[i];
                }
            }

            data[i] = temp;
            sort(data, l, i - 1);
            sort(data, i + 1, r);

        }
    }


}