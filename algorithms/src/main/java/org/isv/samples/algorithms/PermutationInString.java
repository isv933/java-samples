//Permutation in String (Sliding Window)
//
//        Задача:
//        Даны строки s1 и s2.
//        Проверить, содержит ли s2 подстроку, которая является перестановкой s1.
//
//        Пример:
//
//        s1 = "ab", s2 = "eidbaooo"
//        ответ: true ("ba")

package org.isv.samples.algorithms;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PermutationInString {

    public static boolean containsPermutation(String s1, String s2){
        if (s1 == null || s2 == null || s1.length() > s2.length()) {
            return false;
        }
        if (s1.isEmpty()) {
            return true;
        }

        var seenMap = new HashMap<Character,Integer>();
        var needMap = IntStream.range(0,s1.length()).boxed().map(s1::charAt)
                            .collect(Collectors.groupingBy(x->x, Collectors.summingInt(x->1)));
        var total = 0;

        var left = 0;
        for (var right = 0 ; right  < s2.length(); right++){
            var currentChar = s2.charAt(right);
            var result = seenMap.compute(currentChar, (key,count)-> count==null?1:count+1);

            if (needMap.containsKey(currentChar) && needMap.get(currentChar).equals(result)) {
                total++;
            }

            if (total == needMap.size() && (right - left + 1) == s1.length() ){
                return true;
            }

            if ((right - left + 1) == s1.length()) {
                var leftChar = s2.charAt(left);
                if (needMap.containsKey(leftChar) && needMap.get(leftChar).equals(seenMap.get(leftChar))) {
                    total--;
                }
                seenMap.computeIfPresent(leftChar,(key, count)->count-1);
                left = left + 1;
            }

        }
        return false;
    }
    public static void test(){
        System.out.printf("%b==true\n",containsPermutation("ab", "eidbaooo"));
        System.out.printf("%b==true\n",containsPermutation("aab", "eidabaooo"));
        System.out.printf("%b==false\n",containsPermutation("ab", "ebdao"));
        System.out.printf("%b==false\n",containsPermutation("aab", "cabbab"));
        System.out.printf("%b==true\n",containsPermutation("baba", "ccaabbac"));
        System.out.printf("%b==false\n",containsPermutation("ab", "axb"));


    }

}
