package org.isv.samples.algorithms;


import java.util.*;
import java.util.stream.Collectors;

public class TopKFrequentWords {
    public static List<String> topKFrequent(String[] words, int k) {
        var hmap = new HashMap<String, Integer>();
        var heap = new PriorityQueue<>
                (Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue).reversed()
                        .thenComparing(Map.Entry::getKey));
        for (var word : words) {
            hmap.compute(word, (key, value) -> value == null ? 1 : value + 1);
        }

        heap.addAll(hmap.entrySet());

        return heap.stream().limit(k).map(Map.Entry::getKey).toList();

    }

    public static void test() {
        System.out.println(toString(topKFrequent(new String[]{"i", "love", "leetcode", "i", "love", "coding"}, 2)));
    }


    private static String toString(Collection<String> words) {

        return words.stream().collect(Collectors.joining(",", "[", "]"));


    }


}
