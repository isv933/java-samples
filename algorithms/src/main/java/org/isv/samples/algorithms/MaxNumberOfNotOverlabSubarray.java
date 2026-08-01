package org.isv.samples.algorithms;

import java.util.HashMap;

// Given an array nums and an integer target, return the maximum number of non-empty non-overlapping subarrays
// such that the sum of values in each subarray is equal to target.

public class MaxNumberOfNotOverlabSubarray {
    static class Solution {
        public int maxNonOverlapping(int[] nums, int target) {
            var seen = new HashMap<Integer, Integer>();
            var total = 0;
            var res = 0;
            var minPos = -1;

            seen.put(0, -1);
            for (var right = 0; right < nums.length; right++) {
                total += nums[right];
                var left = seen.get(total - target);
                if (left != null && left >= minPos) {
                    res++;
                    minPos = right;
                }

                seen.put(total, right);
            }

            return res;
        }
    }
}
