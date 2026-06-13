package org.isv.samples.algorithms;

import java.util.HashMap;

public class TwoSingleNum {
    public static class Solution {
        public int[] twoSum(int[] nums, int target) {
            var seen = new HashMap<Integer, Integer>();
            for (var i = 0; i < nums.length; i++) {
                var diff = target - nums[i];

                if (seen.containsKey(diff)) {
                    return new int[]{seen.get(diff), i};
                }

                seen.put(nums[i], i);
            }
            return new int[]{};
        }
    }
}
