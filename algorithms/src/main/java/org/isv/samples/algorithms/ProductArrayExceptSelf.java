package org.isv.samples.algorithms;

import java.util.Arrays;

public class ProductArrayExceptSelf {
    public static class Solution {
        public static int[] productExceptSelf(int[] nums) {
            if (nums.length<1){
                return new int[]{};
            }
            var s = new int[nums.length];
            var p = new int[nums.length];
            var res = new int[nums.length];

            for(var i = 0 ; i < nums.length; i++) {
                p[i] = i ==0? nums[i] : p[i-1] * nums[i];
                s[nums.length-i-1] = i ==0 ? nums[nums.length-1] : s[nums.length-i] *  nums[nums.length-i-1];
            }

            for(var i = 0 ; i < nums.length; i++) {
                var suffix = i == nums.length-1?1: s[i+1];
                var prefix = i == 0?1 : p[i-1];
                res[i] =  suffix * prefix;
            }

            return res;
        }
    }
    public static void test(){
        System.out.println(Arrays.stream(Solution.productExceptSelf(new int[]{1,2,3,4})).boxed().toList());
        System.out.println(Arrays.stream(Solution.productExceptSelf(new int[]{1,2})).boxed().toList());
    }



}
