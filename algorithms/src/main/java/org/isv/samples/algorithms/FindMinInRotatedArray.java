package org.isv.samples.algorithms;

public class FindMinInRotatedArray {

    public static class Solution {
        public int findMin(int[] nums) {
            if (nums.length<1){
                return -1;
            }
            var pivot = findPivot(nums,0, nums.length-1);

            return pivot ==-1 ? nums[0]: Math.min(nums[0], nums[pivot]);
        }
        private static int findPivot(int[] input, int start, int end) {
            if (start < 0 || start > end || start == end) {
                return -1;
            }

            var mid = (start + end) / 2;

            if (input[mid] > input[mid + 1]) {
                return mid + 1;
            }
            if (input[0] > input[mid]) {
                return findPivot(input, start, mid);
            }

            return findPivot(input, mid + 1, end);
        }

    }

    public static void test() {
        System.out.println(new Solution().findMin(new int[]{3,4,5,1,2}));
        System.out.println(new Solution().findMin(new int[]{1,2,3}));
        System.out.println(new Solution().findMin(new int[]{1,2,5,4,3}));
        System.out.println(new Solution().findMin(new int[]{2,1}));
        System.out.println(new Solution().findMin(new int[]{1,2}));
        System.out.println(new Solution().findMin(new int[]{1}));
    }


}
