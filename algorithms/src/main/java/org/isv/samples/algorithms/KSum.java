//3Sum (Two Pointers)
//
//        Задача:
//        Найти все уникальные тройки чисел, которые дают сумму 0.
//Пример:
//
//        [-1,0,1,2,-1,-4]
//        ответ: [[-1,-1,2], [-1,0,1]]

package org.isv.samples.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KSum {

    public static  List<int[]> get3Sum(int[] nums, int target){
        if (nums == null || nums.length <3){
            return new ArrayList<>();
        }

        return getKSum(Arrays.stream(nums).sorted().toArray(),0,target, 3);
    }


    private static List<int[]> getKSum(int[] nums, int startPos, int target, int k) {
        if (k ==2){
            return get2Sum(nums, startPos, target);
        }

        var result = new ArrayList<int[]>();

        for (var i = startPos; i<nums.length - (k-1); i++){
            var current = i;

            if (current >startPos && nums[current]==nums[current-1]){
                continue;
            }

            var res = getKSum(nums, current+1,target - nums[current],k-1);
            if (!res.isEmpty()) {
                result.addAll(res.stream().map(x -> concatSumResult(nums[current], x)).toList());
            }
        }
        return result;
    }
    private static List<int[]> get2Sum(int[] nums, int startPos, int target) {
        var p1 = startPos;
        var p2 = nums.length -1;
        var result = new ArrayList<int[]>();

        while (p1 < p2) {
            var first = nums[p1];
            var second = nums[p2];
            if (first + second < target) {
                p1++;
            } else if (first + second > target) {
                p2--;
            } else {
                    result.add(new int[]{first, second});
                    while (p1 < p2 && nums[p1] == first) {
                         p1++;
                     }
                    while (p1 < p2 && nums[p2] == second) {
                        p2--;
                    }
            }
        }

            return result;
    }

    private static int[] concatSumResult(int num, int[] nums) {
        var result = new int[nums.length + 1];
        result[0] = num;
        System.arraycopy(nums, 0, result, 1, nums.length);
        return result;
    }

    public static void test(){
        System.out.printf("3Sum: %s == [[-1, 0, 1], [-1, -1, 2]]\n", formatTestResult(get3Sum(new int[]{-1,0,1,2,-1,-4}, 0)));
        System.out.printf("3Sum: %s == [[-4, 2, 2], [-1, 0, 1], [-1, -1, 2]]\n", formatTestResult(get3Sum(new int[]{-1,0,0,1,2,2,-1,-4}, 0)));
        System.out.printf("3Sum: %s == []\n", formatTestResult(get3Sum(new int[]{0,1,1}, 0)));
        System.out.printf("3Sum: %s == [[0, 0, 0]]\n", formatTestResult(get3Sum(new int[]{0,0,0}, 0)));
    }

    private static String formatTestResult(List<int[]> result) {
        return result.stream().map(x-> Arrays.stream(x).mapToObj(String::valueOf).collect(Collectors.joining(", ","[","]")))
                                .collect(Collectors.joining(", ","[","]"));

    }

}
