//Subarray Sum Equals K (Prefix Sum / HashMap)
//
//        Задача:
//        Дан массив чисел и число k.
//        Найти количество подмассивов, сумма которых равна k.
//
//        Пример:
//
//        [1,1,1], k = 2
//        ответ: 2


package org.isv.samples.algorithms;

import java.util.HashMap;

public class ContiniousSubArraySumEqualsK {
    public static int getSubarraysCount(int[] input, int k){
        var prefixCounter = new HashMap<Integer,Integer>();
        var currentPrefixSum = 0;
        var result = 0;
        prefixCounter.put(0,1);
        for (var i : input) {
            currentPrefixSum += i;
            result += prefixCounter.getOrDefault(currentPrefixSum - k, 0);
            prefixCounter.put(currentPrefixSum, prefixCounter.getOrDefault(currentPrefixSum, 0) + 1);
        }
        return result;
    }

    public static void test(){
        System.out.printf("SubArraySum: %d == %d\n", getSubarraysCount(new int[]{1,1,1}, 2), 2);
        System.out.printf("SubArraySum: %d == %d\n", getSubarraysCount(new int[]{1,2,3}, 3), 2);
        System.out.printf("SubArraySum: %d == %d\n", getSubarraysCount(new int[]{1}, 1), 1);
        System.out.printf("SubArraySum: %d == %d\n", getSubarraysCount(new int[]{1,-1,0}, 0), 3);
        System.out.printf("SubArraySum: %d == %d\n", getSubarraysCount(new int[]{1,1,-1,2,1}, 3), 3);
        System.out.printf("SubArraySum: %d == %d\n", getSubarraysCount(new int[]{-5,10,-2}, 3), 1);
    }
}
