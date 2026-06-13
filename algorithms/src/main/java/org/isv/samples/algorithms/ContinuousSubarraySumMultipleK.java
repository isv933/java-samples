//Continuous Subarray Sum (Prefix Sum / HashMap)
//
//        Задача:
//        Проверить, существует ли подмассив длины ≥ 2, сумма которого кратна k.

package org.isv.samples.algorithms;

import java.util.HashMap;

public class ContinuousSubarraySumMultipleK {
    public static boolean isHasMultipleKArray(int[] input, int k) {
        if (input == null) {
            return false;
        }
        var index = new HashMap<Integer, Integer>();
        var prefix = 0;
        index.put(prefix, -1);
        for (var right = 0; right < input.length; right++) {
            prefix += input[right];
            var multiplicity = k == 0 ? prefix : prefix % k;
            if (index.containsKey(multiplicity)) {
                var left = index.get(multiplicity);
                if (right - left >= 2) {
                    return true;
                }
            } else {
                index.put(multiplicity, right);
            }
        }

        return false;
    }

    public static void test() {

        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(null, 1), false);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{0}, 0), false);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{0, 0}, 0), true);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{0, 0, 0}, 0), true);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{0, 1, 0}, 0), false);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{1, 1, 4}, -5), true);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{1, 1, 4}, 5), true);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{6, 1}, 6), false);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{2}, 2), false);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{0, 0}, 2), true);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{0, 1}, 2), false);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{1, 0}, 2), false);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{2, 1}, 2), false);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{1, 2}, 2), false);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{2, 1}, 2), false);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{1, 1}, 2), true);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{23, 2, 4, 6, 7}, 6), true);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{23, 2, 6, 4, 7}, 13), false);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{1, 3, 6, 7, 9, 10, 16, 17}, 7), true);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{1, 2, 12}, 6), false);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{1, 2, 3}, 5), true);
        System.out.printf("ContinuousSubarraySum: %b == %b\n", isHasMultipleKArray(new int[]{1, 2, 3}, 6), true);
    }
}
