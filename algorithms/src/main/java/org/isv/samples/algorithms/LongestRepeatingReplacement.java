//Longest Repeating Character Replacement (Sliding Window)
//
//        Задача:
//        Дана строка s и число k. Можно заменить не более k символов в строке.
//        Найти длину самой длинной подстроки, которую можно превратить в строку из одинаковых символов.
//
//        Пример:
//
//        s = "AABABBA", k = 1
//        ответ: 4 ("AABA")
//        sliding window
//        максимум одного символа


package org.isv.samples.algorithms;

import java.util.HashMap;

import static java.lang.Math.max;

public class LongestRepeatingReplacement {
    public static int getMaxLength(String s, int k) {
        var counter = new HashMap<Character, Integer>();
        var left = 0;
        var maxLen = 0;
        var maxFreq = 0;

        for (var i = 0; i < s.length(); i++) {
            var currentChar = s.charAt(i);
            var curCount = counter.compute(currentChar, (key, count) -> count == null ? 1 : count + 1);
            maxFreq = max(maxFreq, curCount);

            while ((i - left + 1) - maxFreq > k) {
                counter.computeIfPresent(s.charAt(left), (key, count) -> count - 1);
                left = left + 1;
            }
            maxLen = max(maxLen, i - left + 1);
        }

        return maxLen;
    }

    public static void test() {
        System.out.printf("%d==4\n", getMaxLength("ABAB", 2));
        System.out.printf("%d==5\n", getMaxLength("AABBB", 2));
        System.out.printf("%d==3\n", getMaxLength("ABCB", 1));
        System.out.printf("%d==6\n", getMaxLength("AABBCCC", 3));
    }

}
