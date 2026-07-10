package org.isv.samples.algorithms;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class SingleNumber {
    public static class Solution {
        public static int singleNumber(int[] nums) {
            return Arrays.stream(nums).boxed().collect(Collectors
                            .groupingBy(x -> x, Collectors.counting()))
                    .entrySet().stream().filter(x -> x.getValue() == 1)
                    .findFirst().orElseThrow().getKey();

        }

    }

    public static void test(){
        System.out.println(Solution.singleNumber(new int[]{1,2,3,3,1}));

    }
}
