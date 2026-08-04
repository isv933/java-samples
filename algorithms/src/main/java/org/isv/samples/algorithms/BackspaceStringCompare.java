// Given two strings s and t, return true if they are equal when both are typed into empty text editors.
// '#' means a backspace character.    Note that after backspacing an empty text, the text will continue empty.
// Input: s = "ab#c", t = "ad#c"
//        Output: true
//        Explanation: Both s and t become "ac".

package org.isv.samples.algorithms;

import java.util.ArrayDeque;
import java.util.Deque;

public class BackspaceStringCompare {
    static class SolutionStack {
        private static Deque<Character> createQueue(String s) {
            var queue = new ArrayDeque<Character>();

            for (var i = 0; i < s.length(); i++) {
                var ch = s.charAt(i);
                if (ch == '#') {
                    queue.pollFirst();
                } else {
                    queue.addFirst(ch);
                }
            }
            return queue;
        }

        public boolean backspaceCompare(String s, String t) {
            var leftQueue = createQueue(s);
            var rightQueue = createQueue(t);

            while (!leftQueue.isEmpty() && !rightQueue.isEmpty()) {
                if (leftQueue.pollFirst() != rightQueue.pollFirst()) {
                    return false;
                }
            }

            return leftQueue.isEmpty() && rightQueue.isEmpty();
        }
    }

    static class Solution2Pointers {
        public boolean backspaceCompare(String s, String t) {
            var left = s.length() - 1;
            var right = t.length() - 1;

            while (left >= 0 || right >= 0) {
                left = skipBackSpaces(s, left);
                right = skipBackSpaces(t, right);

                if (!compare(s, left, t, right)) {
                    return false;
                }

                if (left >= 0) {
                    left--;
                }

                if (right >= 0) {
                    right--;
                }
            }
            return left == right;
        }

        private static boolean compare(String leftS, int left, String rightS, int right) {
            if (left < 0 && right >= 0 || left >= 0 && right < 0) {
                return false;
            }

            return (left < 0 && right < 0) || leftS.charAt(left) == rightS.charAt(right);
        }

        private static int skipBackSpaces(String s, int currentPos) {
            var spaceCount = 0;

            while (currentPos >= 0 && (s.charAt(currentPos) == '#' || spaceCount > 0)) {
                if (s.charAt(currentPos) == '#') {
                    spaceCount++;
                } else {
                    spaceCount--;
                }
                currentPos--;
            }

            return currentPos;
        }
    }
}
