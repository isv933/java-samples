package org.isv.samples.algorithms;

//
// Given a string s of zeros and ones, return the maximum score after splitting the string into two non-empty
// substrings (i.e. left substring and right substring).
//
// The score after splitting a string is the number of zeros in the left substring plus the number of ones
// in the right substring.

public class MaximumScoreAfterSplitString {
    public static class Solution {
        public int maxScore(String s) {
            var prefix = new int[s.length()];
            for (var i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '0') {
                    prefix[i] = i == 0 ? 1 : prefix[i - 1] + 1;
                } else {
                    prefix[i] = i == 0 ? 0 : prefix[i - 1];
                }
            }
            var maxCount = 0;
            var total = 0;
            for (var i = s.length() - 1; i > 0; i--) {
                if (s.charAt(i) == '1') {
                    total += 1;
                }

                prefix[i - 1] += total;
                maxCount = Math.max(maxCount, prefix[i - 1]);
            }

            return maxCount;
        }
    }
}
