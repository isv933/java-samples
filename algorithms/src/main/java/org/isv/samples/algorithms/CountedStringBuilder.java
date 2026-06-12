package org.isv.samples.algorithms;

import java.util.LinkedHashMap;

public class CountedStringBuilder {
    public static String convertToString(String s){
        var hashMap = new LinkedHashMap<Character, Integer>();
        var result = new StringBuilder();
        for(var i = 0; i < s.length(); i++){
            hashMap.compute(s.charAt(i), (key,value)->value==null?1:value+1);
        }

        for(var entry : hashMap.entrySet()) {
            if (entry.getValue()>1) {
                    result.append(entry.getValue());
            }
                result.append(entry.getKey());
        }

        return result.toString();
    }

    public static String convertToStringB(String s){
        if (s.length()<=1) {
            return s;
        }

        var prev = s.charAt(0);
        var count = 1;
        var result = new StringBuilder();

        for(var i =1 ; i < s.length(); i++) {
            if (s.charAt(i) == prev) {
                count++;
            } else {
                if (count>1) {
                    result.append(count);
                }
                result.append(prev);
                prev = s.charAt(i);
                count = 1;
            }
        }

        if (count>1) {
            result.append(count);
        }
        result.append(prev);


        return result.toString();
    }



    public static void test(){
        System.out.println(convertToString("AABCDDE"));
        System.out.println(convertToStringB("AABCDDE"));
    }
}
