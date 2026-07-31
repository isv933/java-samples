package org.isv.samples.algorithms;

//Given an array of integers nums, calculate the pivot index of this array.
//The pivot index is the index where the sum of all the numbers strictly to the left of the
// index is equal to the sum of all the numbers strictly to the index's right.
//If the index is on the left edge of the array, then the left sum is 0 because there are no elements to the left.
// This also applies to the right edge of the array.
//Return the leftmost pivot index. If no such index exists, return -1.

public class FindPivotIndex {
    public static void test() {
        System.out.println(FindPivotIndex.Solution.pivotIndex((new int[]{1, 7, 3, 6, 5, 6})));
        System.out.println(FindPivotIndex.Solution.pivotIndex((new int[]{1, 2, 3})));
        System.out.println(FindPivotIndex.Solution.pivotIndex((new int[]{2, 1, -1})));
    }

    static class Solution {
        public static int pivotIndex(int[] nums) {
            var prefix = new int[nums.length];
            for (var i = 0; i < nums.length; i++) {
                prefix[i] = i == 0 ? nums[i] : nums[i] + prefix[i - 1];
            }
            for (var i = 0; i < prefix.length; i++) {
                var left = prefix[i] - nums[i];
                var right = prefix[prefix.length - 1] - prefix[i];

                if (left == right) {
                    return i;
                }
            }

            return -1;
        }
    }

}
