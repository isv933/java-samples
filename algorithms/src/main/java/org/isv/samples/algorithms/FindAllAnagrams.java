package org.isv.samples.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindAllAnagrams {
    public static void test() {
        System.out.println(Solution.findAnagrams("cbaebabacd", "abc"));
        System.out.println(Solution.findAnagrams("abab", "ab"));
        System.out.println(Solution.findAnagrams("dinitrophenylhydrazinetrinitrophenylmethylnitramine", "trinitrophenylmethylnitramine"));
    }

    static class Solution {
        public static List<Integer> findAnagrams(String s, String p) {
            var pattern = initializePattern(p); //it works faster than stream API
            var seen = new HashMap<Integer, Integer>();
            var result = new ArrayList<Integer>();
            var left = 0;
            var count = 0;
            for (var right = 0; right < s.length(); right++) {
                var currentChar = Integer.valueOf(s.charAt(right));
                var needCount = pattern.get(currentChar);
                if (needCount != null) {
                    var seenCount = seen.compute(currentChar, (ch, cnt) -> cnt == null ? 1 : cnt + 1);
                    if (needCount.intValue() == seenCount.intValue()) {
                        count++;
                    }
                }
                while (right - left > p.length() - 1) {
                    var leftChar = Integer.valueOf(s.charAt(left));
                    var seenLeft = seen.computeIfPresent(leftChar, (ch, cnt) -> cnt - 1);
                    if (seenLeft != null && seenLeft == pattern.get(leftChar) - 1) {
                        count--;
                    }
                    left++;
                }
                if (count == pattern.size()) {
                    result.add(left);
                }
            }
            return result;
        }
        private static HashMap<Integer, Integer> initializePattern(String s) {
            var result = new HashMap<Integer, Integer>();
            for (var i = 0; i < s.length(); i++) {
                result.compute((int) s.charAt(i), (ch, cnt) -> cnt == null ? 1 : cnt + 1);
            }

            return result;
        }
    }

}
