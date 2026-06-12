//Max Sliding Window (Deque)
//
//        Задача:
//        Дан массив чисел и размер окна k.
//        Найти максимальное число в каждом скользящем окне.
//
//        Пример:
//
//        [1,3,-1,-3,5,3,6,7], k = 3
//        ответ: [3,3,5,5,6,7]

package org.isv.samples.algorithms;

import java.util.ArrayDeque;


public class MaxKArray {
    public static int[] maxSlidingWindow(int[] nums, int k) {
        var queue = new ArrayDeque<Integer>() {};
        if (k>nums.length){
            return new int[0];
        }
        var result = new int[nums.length - k + 1];
        for(var i = 0; i<nums.length; i++) {

            while (!queue.isEmpty() && queue.peekFirst()<i-k+1) {
                queue.removeFirst();
            }

            while (!queue.isEmpty() && nums[queue.peekLast()] <= nums[i]) {
                queue.removeLast();
            }

            queue.addLast(i);
            if (i>=k-1) {
                result[i-k+1] = nums[queue.peekFirst()];
            }
         }
        return result;
   }
    public static void test(){
        System.out.printf("MaxKArray: %s == [3,3,5,5,6,7]\n", java.util.Arrays.toString(maxSlidingWindow(new int[]{1,3,-1,-3,5,3,6,7}, 3)));
        System.out.printf("MaxKArray: %s == [1]\n", java.util.Arrays.toString(maxSlidingWindow(new int[]{1}, 1)));
        System.out.printf("MaxKArray: %s == [10,10,9,2]\n", java.util.Arrays.toString(maxSlidingWindow(new int[]{9,10,9,-7,-4,-8,2,-6}, 5)));
    }

 }

