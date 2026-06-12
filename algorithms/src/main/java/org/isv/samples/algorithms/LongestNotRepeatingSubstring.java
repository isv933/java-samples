//Longest Substring Without Repeating Characters
//
//        Задача:
//        Дана строка s. Найти длину самой длинной подстроки без повторяющихся символов.
//
//        Пример:
//
//        s = "abcabcbb"
//        ответ: 3 ("abc")

package org.isv.samples.algorithms;

import java.util.HashMap;

import static java.lang.Math.max;

public class LongestNotRepeatingSubstring {
    public static int getSubstringLength(String s){
        var passedChars = new HashMap<Character, Integer>();
        var maxLength = 0;
        var left = 0;
        for(var i=0; i < s.length(); i++){
            var currentChar = s.charAt(i);
            if (passedChars.containsKey(currentChar)){
                left = max(left, passedChars.get(currentChar)+1);
            }
            maxLength = max(maxLength, i - left+1);
            passedChars.put(currentChar,i);
        }

        return maxLength;
    }

    public static  void test(){
        System.out.printf("%d==3\n",getSubstringLength("abcabcbb"));
        System.out.printf("%d==1\n",getSubstringLength("bbbbb"));
        System.out.printf("%d==3\n",getSubstringLength("pwwkew"));
        System.out.printf("%d==0\n",getSubstringLength(""));
        System.out.printf("%d==1\n",getSubstringLength("a"));
        System.out.printf("%d==2\n",getSubstringLength("au"));
        System.out.printf("%d==2\n",getSubstringLength("dvdf"));
        System.out.printf("%d==5\n",getSubstringLength("tmmzuxt"));
    }




}
