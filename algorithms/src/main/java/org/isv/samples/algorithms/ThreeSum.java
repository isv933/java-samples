package org.isv.samples.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeSum {
        public static class Solution {
            public static List<List<Integer>> threeSum(int[] nums) {
                Arrays.sort(nums);
                var result = new ArrayList<List<Integer>>();

                for (var i = 0; i < nums.length; i++) {
                    var current = nums[i];
                    if (i == 0 || (nums[i - 1] != current)) {
                        var twoResult = twoSum(nums, i + 1, -current);
                        for (var res : twoResult) {
                            res.add(current);
                        }
                        result.addAll(twoResult);
                    }
                }

                return result;
            }

            private static List<List<Integer>> twoSum(int[] input, int startPos, int target) {
                var left = startPos;
                var right = input.length - 1;
                var result = new ArrayList<List<Integer>>();

                while (left < right) {
                    var leftV = input[left];
                    var rightV = input[right];
                    var total = leftV + rightV;

                    if (total < target) {
                        left++;

                    } else if (total > target) {
                        right--;
                    } else {
                        result.add(new ArrayList<>() {{
                            add(leftV);
                            add(rightV);
                        }});
                        while (left < input.length && input[left] == leftV) {
                            left++;
                        }
                        while (right > startPos && input[right] == rightV) {
                            right--;
                        }
                    }
                }
                return result;
            }
        }

        public static void test() {
            System.out.println(Solution.threeSum(new int[]{-3, -1, 0, 1, 2, 3}));
        }
}

