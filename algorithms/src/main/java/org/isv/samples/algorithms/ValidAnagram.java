package org.isv.samples.algorithms;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class ValidAnagram {
    public static boolean isAnagramUsingSort(String s, String t) {
        var a = s.toCharArray();
        var b = t.toCharArray();

        Arrays.sort(a);
        Arrays.sort(b);

        return Arrays.equals(a, b);
    }

    public static boolean isValidAnagramUsingHeap(String s, String t){
        var sQueue = new PriorityQueue<Character>(Comparator.comparingInt(x->x));
        var tQueue = new PriorityQueue<Character>(Comparator.comparingInt(x->x));
        if (s.length()!=t.length()) {
            return false;
        }

        while (!sQueue.isEmpty() && !tQueue.isEmpty()) {
            if (sQueue.poll()!=tQueue.poll()) {
                return false;
            }
        }
        return sQueue.isEmpty() && tQueue.isEmpty();
    }


    public static boolean isValidAnagram(String s, String t) {
        var ms = normalizeString(s);
        var ts = normalizeString(t);

        return Arrays.equals(ms, ts);
    }

    private static int[] normalizeString(String s) {
        var result = new int['z' - 'a' + 1];
        for (var i = 0; i < s.length(); i++) {
            result[s.charAt(i) - 'a']++;
        }

        return result;
    }

    public static void test() {
        System.out.println(ValidAnagram.isAnagramUsingSort("anagram", "nagaram"));
        System.out.println(ValidAnagram.isAnagramUsingSort("abca", "acab"));
        System.out.println(ValidAnagram.isAnagramUsingSort("abca", "acb"));

        System.out.println(ValidAnagram.isValidAnagram("anagram", "nagaram"));
        System.out.println(ValidAnagram.isValidAnagram("abca", "acab"));
        System.out.println(ValidAnagram.isValidAnagram("abca", "acb"));

        System.out.println(ValidAnagram.isValidAnagramUsingHeap("anagram", "nagaram"));
        System.out.println(ValidAnagram.isValidAnagramUsingHeap("abca", "acab"));
        System.out.println(ValidAnagram.isValidAnagramUsingHeap("abca", "acb"));

    }
}
