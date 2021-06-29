package org.luvx.algorithm.array;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;


/**
 * 奇数在偶数的前面
 * 2,1,4,3,5,6
 * ↓
 * 1,3,5,2,4,6
 */
public class ModifiedArray {
    public static void modifiedArray(int[] array) {
        int length = array.length;
        int i = length - 1;
        while (i >= 1) {
            if (array[i - 1] % 2 == 0 && array[i] % 2 != 0) {
                ArrayUtils.swap(array, i - 1, i);
                for (int j = i; j < length - 1; j++) {
                    if (array[j] % 2 == 0 && array[j + 1] % 2 != 0) {
                        ArrayUtils.swap(array, j, j + 1);
                    }
                }
            }
            i--;
        }
    }

    public static void main(String[] args) {
        int[] array = {2, 1, 4, 3, 5, 6};
        System.out.println(Arrays.toString(array));
        modifiedArray(array);
        System.out.println(Arrays.toString(array));
    }
}
