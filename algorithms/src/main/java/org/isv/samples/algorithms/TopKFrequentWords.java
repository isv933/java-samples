package org.isv.samples.algorithms;


import java.util.*;

public class TopKFrequentWords {
    public static List<String> topKFrequent(String[] words, int k) {
        var hmap = new HashMap<String, Integer>();
        var result = new  ArrayList<String>();

        var heap = new PriorityQueue<>
                (Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue).reversed()
                        .thenComparing(Map.Entry::getKey));
        for (var word : words) {
            hmap.compute(word, (key, value) -> value == null ? 1 : value + 1);
        }
        heap.addAll(hmap.entrySet());

        for(var i = 0; i <k && !heap.isEmpty(); i++) {
            result.add(heap.poll().getKey());
        }

        return result;
    }

    public static void test() {
        System.out.println(topKFrequent(new String[]{"i", "love", "leetcode", "i", "love", "coding"}, 2));
    }

}
