//Minimum Window Substring (Sliding Window)
//
//        Задача:
//        Даны две строки s и t.
//        Найти наименьшее окно (подстроку) в s, которое содержит все символы из t, включая дубликаты.
//        Дубликаты учитывать по количеству, порядок следования не имеет значения
//        Если такого окна нет, вернуть пустую строку.
//
//        Пример:
//
//        s = "ADOBECODEBANC", t = "ABC"
//        ответ: "BANC"


package org.isv.samples.algorithms;

import java.util.HashMap;

public class MinWindowSubstring {
    public static String getMinSubstring(String s, String t) {
        var seenChars = new HashMap<Character, Integer>();
        var needChars = new HashMap<Character, Integer>();
        var formed = 0;
        var min_pos = -1;
        var max_pos = -1;
        var left = 0;
        var right = 0;

        for (var i = 0; i < t.length(); i++) {
            needChars.compute(t.charAt(i), (key, count) -> count == null ? 1 : count + 1);
        }

        while (right < s.length()) {
            var currentChar = s.charAt(right);
            if (needChars.containsKey(currentChar)) {
                var resCount = seenChars.compute(currentChar, (key, count) -> count == null ? 1 : count + 1);

                if (resCount.equals(needChars.get(currentChar))) {
                    formed++;
                }

                if (formed >= needChars.size()) {
                    while (left < right) {
                        var leftChar = s.charAt(left);
                        if (seenChars.containsKey(leftChar)) {
                            if (seenChars.get(leftChar).compareTo(needChars.get(leftChar)) <= 0) {
                                break;
                            }
                            seenChars.computeIfPresent(leftChar, (key, count) -> count - 1);
                        }
                        left = left + 1;
                    }
                    if ((max_pos == -1 && min_pos == -1) || (right - left < max_pos - min_pos)) {
                        min_pos = left;
                        max_pos = right;
                    }
                }
            }
            right = right + 1;
        }

        return min_pos != -1 ? s.substring(min_pos, max_pos + 1) : "";
    }

    public static void test() {
        System.out.printf("MinWindowSubstring: %s == %s\n", getMinSubstring("ADOBECODEBANC", "ABC"), "BANC");
        System.out.printf("MinWindowSubstring: %s == %s\n", getMinSubstring("a", "a"), "a");
        System.out.printf("MinWindowSubstring: %s == %s\n", getMinSubstring("a", "aa"), "");
        System.out.printf("MinWindowSubstring: %s == %s\n", getMinSubstring("ADOBECODEBANC", "ABCC"), "CODEBANC");
        System.out.printf("MinWindowSubstring: %s == %s\n", getMinSubstring("ADOBECODEBANC", "ADEC"), "ADOBEC");
        System.out.printf("MinWindowSubstring: %s == %s\n", getMinSubstring("ADOBECODEBANC", "ABBC"), "BECODEBA");
        System.out.printf("MinWindowSubstring: %s == %s\n", getMinSubstring("cabwefgewcwaefgcf", "cae"), "cwae");
    }


}
