package org.isv.samples.algorithms;


import java.util.Comparator;
import java.util.List;

public class PointsWithCars {
    static class Solution {
        public static int numberOfPoints(List<List<Integer>> nums) {
            var result = 0;
            var left = 0;

            for(var num : nums.stream().sorted(Comparator.comparing(x->x.get(0))).toList()){
                if (num.get(1)>=left) {
                    result+= (num.get(1) -  Math.max(left,num.get(0))+1);
                    left = num.get(1)+1;
                }
            }
            return result;
        }
    }

    public static void test(){
        System.out.println(Solution.numberOfPoints(List.of(List.of(3,6),List.of(1,5), List.of(4,7))));
        System.out.println(Solution.numberOfPoints(List.of(List.of(1,3),List.of(5,8))));
        System.out.println(Solution.numberOfPoints(List.of(List.of(4,4),List.of(9,10),List.of(9,10),List.of(3,8))));
    }

}
