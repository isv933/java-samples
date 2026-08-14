// Дан массив чисел произвольного порядка. Можно переставлять местами соседние числа одной четности. Определить является ли
// массив сортируемым.
// Пример:
// [0,1 2,5] -> true
// [0,5,3,1,8,6] -> true
// [0,5,3,1,8,2] -> false

package org.isv.samples.algorithms;

import java.util.HashMap;

public class CanBeSorted {

    public static boolean canBeSortedMaxOddEven(int[] data) {
        var maxSeen = new HashMap<Parity, Integer>();

        for (var i : data) {
            var parity = i % 2 == 0 ? Parity.EVEN : Parity.ODD;

            if (maxSeen.containsKey(parity.reverse()) && i < maxSeen.get(parity.reverse())) {
                return false;
            }
            maxSeen.compute(parity, (k, v) -> v == null ? i : Math.max(v, i));
        }
        return true;
    }

    public static boolean canBeSortedChangeParity(int[] data) {
        if (data.length == 0) {
            return true;
        }

        var prev = data[0];
        var prevParity = prev % 2 == 0;

        for (var i = 1; i < data.length; i++) {
            var currentParity = data[i] % 2 == 0;
            if (currentParity != prevParity) {
                if (data[i] < prev) {
                    return false;
                }
                prevParity = currentParity;
            }
            prev = Math.max(prev, data[i]);
        }

        return true;
    }

    public static void test() {
        System.out.println(canBeSortedMaxOddEven(new int[]{2, 0, 3, 4}));
        System.out.println(canBeSortedMaxOddEven(new int[]{2, 0, 3, 6, 4}));
        System.out.println(canBeSortedMaxOddEven(new int[]{2, 0, 3, 6, 4, 7}));
        System.out.println(canBeSortedMaxOddEven(new int[]{2, 0, 3, 6, 4, 1}));
        System.out.println(canBeSortedMaxOddEven(new int[]{5, 2, 1}));
        System.out.println("-----------------------------------");
        System.out.println(canBeSortedChangeParity(new int[]{2, 0, 3, 4}));
        System.out.println(canBeSortedChangeParity(new int[]{2, 0, 3, 6, 4}));
        System.out.println(canBeSortedChangeParity(new int[]{2, 0, 3, 6, 4, 7}));
        System.out.println(canBeSortedChangeParity(new int[]{2, 0, 3, 6, 4, 1}));
        System.out.println(canBeSortedChangeParity(new int[]{5, 2, 1}));


    }

    private enum Parity {
        ODD,
        EVEN;

        public Parity reverse() {
            return this == ODD ? EVEN : ODD;
        }
    }
}
