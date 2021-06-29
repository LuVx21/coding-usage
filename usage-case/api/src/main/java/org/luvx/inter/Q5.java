package org.luvx.inter;

import java.util.Arrays;

public class Q5 {
    public static boolean findInArray0(int[][] array, int target) {
        for (int i = 0; i < array.length; i++) {
            int j = Arrays.binarySearch(array[i], target);
            if (-1 != j) {
                System.out.println("第" + (i + 1) + "行第" + (j + 1) + "个");
            }
        }
        return false;
    }

    /**
     * 效率高
     */
    public static boolean findInArray1(int[][] array, int target) {
        int height = array.length - 1;
        int i = 0;
        while (i < array[0].length && height >= 0) {
            if (array[height][i] < target) {
                i++;
            } else if (array[height][i] > target) {
                height--;
            } else {
                System.out.println("第" + (height + 1) + "行第" + (i + 1) + "个");
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[][] array = {{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}};

        findInArray0(array, 6);
        findInArray1(array, 6);
        // printArray(array);
    }
}
