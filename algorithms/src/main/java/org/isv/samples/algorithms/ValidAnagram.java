package org.isv.samples.algorithms;

import java.util.Arrays;

public class ValidAnagram {
    public static boolean isAnagramUsingSort(String s, String t) {
        var a = s.toCharArray();
        var b =  t.toCharArray();

        Arrays.sort(a);
        Arrays.sort(b);

        return Arrays.equals(a,b);
    }

    public static boolean isValidAnagram(String s, String t){
        var ms = normalizeString(s);
        var ts = normalizeString(t);

        return Arrays.equals(ms,ts);
    }

    private static int[] normalizeString(String s){
        var result = new int['z' - 'a'+1];
        for(var i = 0; i < s.length(); i++) {
            result[s.charAt(i)- 'a']++;
        }

        return result;
    }

    public static void test(){
        System.out.println(ValidAnagram.isAnagramUsingSort("anagram", "nagaram"));
        System.out.println(ValidAnagram.isAnagramUsingSort("abca", "acab"));
        System.out.println(ValidAnagram.isAnagramUsingSort("abca", "acb"));

        System.out.println(ValidAnagram.isValidAnagram("anagram", "nagaram"));
        System.out.println(ValidAnagram.isValidAnagram("abca", "acab"));
        System.out.println(ValidAnagram.isValidAnagram("abca", "acb"));
    }
}
