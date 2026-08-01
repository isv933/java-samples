/*
 You are given a string s. We want to partition the string into as many parts as possible so that each letter
 appears in at most one part. For example, the string "ababcc" can be partitioned into ["abab", "cc"],
 but partitions such as ["aba", "bcc"] or ["ab", "ab", "cc"] are invalid.
 Note that the partition is done so that after concatenating all the parts in order, the resultant string should be s.
 Return a list of integers representing the size of these parts.
 1 <= s.length <= 500
 s consists of lowercase English letters.
 */


package org.isv.samples.algorithms;

import java.util.ArrayList;
import java.util.List;

public class PartitionLabels {
    static class Solution {
        public List<Integer> partitionLabels(String s) {
            var seen = new int['z' - 'a' + 1];
            var result = new ArrayList<Integer>();
            for (var i = 0; i < s.length(); i++) {
                seen[s.charAt(i) - 'a'] = i;
            }

            var left = 0;
            var right = 0;
            for (var i = 0; i < s.length(); i++) {
                var current = seen[s.charAt(i) - 'a'];
                right = Math.max(right, current);
                if (right == i) {
                    result.add(right - left + 1);
                    left = i + 1;
                }
            }

            return result;
        }
    }
}
