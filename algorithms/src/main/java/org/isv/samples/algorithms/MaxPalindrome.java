//
// Найти максимальню длину полиндрома который можно получить из заданной строки
//


package org.isv.samples.algorithms;

import java.util.HashMap;

public class MaxPalindrome {

    public static int maxPalindrome(String input) {

        var charsCount = new HashMap<Character, Integer>();
        var pairCount = 0;
        for (var i = 0; i < input.length(); i++) {
            if ((charsCount.compute(input.charAt(i),
                    (k, v) -> v == null ? 1 : v + 1) & 1) == 0) {
                pairCount++;
            }

        }

        var pairCharsCount = pairCount * 2;

        return input.length() - pairCharsCount == 0 ? pairCharsCount : pairCharsCount + 1;
    }


    public static void test() {
        System.out.println(maxPalindrome("abc"));
        System.out.println(maxPalindrome("eaaccbb"));
        System.out.println(maxPalindrome("1232"));

    }


}
