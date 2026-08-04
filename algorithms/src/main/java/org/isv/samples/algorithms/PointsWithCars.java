package org.isv.samples.algorithms;

//You are given a 0-indexed 2D integer array nums representing the coordinates of the cars parking on a number line.
//For any index i, nums[i] = [starti, endi] where starti is the starting point of the ith car and endi is the ending
// point of the ith car.
//Return the number of integer points on the line that are covered with any part of a car.
//Example 1:
//
//        Input: nums = [[3,6],[1,5],[4,7]]
//        Output: 7
//        Explanation: All the points from 1 to 7 intersect at least one car, therefore the answer would be 7.
//        Example 2:
//
//        Input: nums = [[1,3],[5,8]]
//        Output: 7
//        Explanation: Points intersecting at least one car are 1, 2, 3, 5, 6, 7, 8. There are a total of 7 points,
//        therefore the answer would be 7.


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
