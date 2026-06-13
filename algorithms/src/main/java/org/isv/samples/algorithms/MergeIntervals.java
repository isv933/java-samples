package org.isv.samples.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringJoiner;

public class MergeIntervals {
    public static int[][] merge(int[][] intervals) {
        var result = new ArrayList<int[]>();

        if (intervals.length == 0) {
            return new int[][]{};
        }

        Arrays.sort(intervals, Comparator.comparingInt(x -> x[0]));

        var left = intervals[0][0];
        var right = intervals[0][1];

        for (var interval : intervals) {
            if (interval[0] > right) {
                result.add(new int[]{left, right});
                left = interval[0];
                right = interval[1];
            }

            left = Math.min(interval[0], left);
            right = Math.max(interval[1], right);
        }

        result.add(new int[]{left, right});
        return result.toArray(new int[result.size()][]);
    }

    public static void test() {
        System.out.println(arraysToString(MergeIntervals.merge(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}})));
        System.out.println(arraysToString(MergeIntervals.merge(new int[][]{{1, 4}, {4, 5}})));
        System.out.println(arraysToString(MergeIntervals.merge(new int[][]{{4, 7}, {1, 4}})));
    }

    private static String arraysToString(int[][] array) {
        var stringJoiner = new StringJoiner(",", "[", "]");
        for (var i = 0; i < array.length; i++) {
            var valueJoiner = new StringJoiner(",", "[", "]");
            for (var j = 0; j < array[i].length; j++) {
                valueJoiner.add(String.valueOf(array[i][j]));
            }
            stringJoiner.add(valueJoiner.toString());
        }
        return stringJoiner.toString();
    }

}
