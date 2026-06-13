package org.isv.samples.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ArrayIntersector {
    public static int[] intersect(int[] a1, int[] a2) {
        var mapLeft = new HashMap<Integer, Integer>();
        var mapRight = new HashMap<Integer, Integer>();
        var result = new ArrayList<Integer>();

        for (var a : a1) {
            mapLeft.compute(a, (key, val) -> val == null ? 1 : val + 1);
        }

        for (var a : a2) {
            mapRight.compute(a, (key, val) -> val == null ? 1 : val + 1);
        }

        for (var right : mapRight.entrySet()) {
            if (mapLeft.containsKey(right.getKey())) {
                var count = Math.min(right.getValue(), mapLeft.get(right.getKey()));
                for (var i = 0; i < count; i++) {
                    result.add(right.getKey());
                }
            }
        }

        return result.stream().mapToInt(x -> x).toArray();
    }

    public static void test() {

        testResult(new int[]{1, 2, 2, 3}, new int[]{2, 2, 3, 3});
        testResult(new int[]{2, 3}, new int[]{1, 2, 3});
        testResult(new int[]{2}, new int[]{3});


    }

    private static void testResult(int[] a1, int a2[]) {
        System.out.println(Arrays.stream(ArrayIntersector.intersect(a1, a2))
                .mapToObj(String::valueOf).collect(Collectors.joining(",", "[", "]")));
    }

}
