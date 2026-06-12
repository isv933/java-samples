package org.isv.samples.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GroupAnagrams {
    public static List<List<String>> groupAnagrams(String[] strs) {
        var result = new HashMap<String,List<String>>();

        for (var str : strs) {
            var normalStr = toNormalString(str);
            var values = result.get(normalStr);
            if (values != null) {
                values.add(str);
            } else {
                result.put(normalStr, new ArrayList<>(List.of(str)));
            }
        }

        return result.values().stream().toList();
    }

    private static String toNormalString(String s) {
        var counter = new int['z' - 'a' +1];

        for (var i = 0; i < s.length(); i++){
            counter[s.charAt(i) - 'a']++;
        }

        var result = new StringBuilder();
        for(var i = 0 ; i< counter.length; i++){
            for(var j = 0; j < counter[i] && counter[i]>0; j++) {
                result.append(i + 'a');
            }
        }

        return result.toString();
    }


    public static void test(){
        System.out.println(GroupAnagrams.groupAnagrams(new String[] { ""}));
        System.out.println(GroupAnagrams.groupAnagrams(new String[] { "a"}));
        System.out.println(GroupAnagrams.groupAnagrams(new String[] { "eat","tea","tan","ate","nat","bat"}));
    }

    private String getStringFromCollection(List<List<String>> strs){
        return strs.stream().map(x->
                            x.stream().collect(Collectors.joining(",", "[", "]")))
                                .collect(Collectors.joining(",","[", "]"));
    }


}
