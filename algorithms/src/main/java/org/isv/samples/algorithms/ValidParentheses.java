// Valid Parentheses
//        Задача:
//        Проверить, корректна ли строка со скобками.
//
//        Пример:
//
//        "()[]{}" → true
//        "(]" → false
//

package org.isv.samples.algorithms;

import java.util.ArrayDeque;
import java.util.Map;

public class ValidParentheses {
    public static Map<Character, Character> closedBrackets =
            Map.of(')', '(', ']', '[', '}', '{');

    public static boolean isValidParentheses(String s) {
        if (s == null) {
            return true;
        }
        var queue = new ArrayDeque<Character>();

        for (var c : s.toCharArray()) {
            if (c == '{' || c == '(' || c == '[') {
                queue.addLast(c);
            } else if (closedBrackets.containsKey(c)) {

                if (queue.isEmpty() || queue.pollLast() != closedBrackets.get(c)) {
                    return false;
                }
            }
        }
        return queue.isEmpty();
    }

    private static boolean isValidParenthesesRecursion(String s) {
        if (s == null || s.isEmpty()) {
            return true;
        }

        var res = getNextParenthesesPos(s, 1, getClosedChar(s.charAt(0)));
        return res != -1;
    }

    private static int getNextParenthesesPos(String s, int startPos, char ch) {
        if (startPos >= s.length() || ch == 0) {
            return -1;
        }
        for (var i = startPos; i < s.length(); i++) {
            var currentChar = s.charAt(i);
            if (!closedBrackets.containsKey(currentChar) &&
                    !closedBrackets.containsValue(currentChar)) {
                continue;
            }

            if (currentChar == ch) {
                return startPos;
            }
            if (closedBrackets.containsKey(currentChar)) {
                return -1;
            }
            i = getNextParenthesesPos(s, i + 1, getClosedChar(currentChar));
            if (i == -1) {
                return -1;
            }
        }

        return -1;
    }

    private static char getClosedChar(char ch) {
        return switch (ch) {
            case '{' -> '}';
            case '[' -> ']';
            case '(' -> ')';
            default -> 0;
        };
    }


    public static void test() {
        System.out.printf(" : %b == true\n", isValidParentheses(" "));
        System.out.printf("() : %b == true\n", isValidParentheses("()"));
        System.out.printf("()[]{} : %b == true\n", isValidParentheses("()[]{}"));
        System.out.printf("(] : %b == false\n", isValidParentheses("(]"));
        System.out.printf("] : %b == false\n", isValidParentheses("]"));
        System.out.printf("[ : %b == false\n", isValidParentheses("["));
        System.out.printf("{[]} : %b == true\n", isValidParentheses("{[{}{{}}]}"));
        System.out.printf("{[]} : %b == true\n", isValidParentheses("{[]}"));
        System.out.printf("(( : %b == false\n", isValidParentheses("(("));
        System.out.printf("((])) : %b == false\n", isValidParentheses("((]))"));
        System.out.printf("(([)) : %b == false\n", isValidParentheses("(([))"));
        System.out.printf("(({)) : %b == false\n", isValidParentheses("(({))"));
    }
}
