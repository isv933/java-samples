package org.isv.samples.algorithms;

import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

public class ReverseLinkedList {
    public static class ListNode {
        public  int val;
        public ListNode next;
        public ListNode() {}
        public ListNode(int val) { this.val = val; }
        public ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    };

    static  class Solution {
        public ListNode reverseList(ListNode head) {
            var prev = (ListNode) null;
            var current = head;

            while (current!=null) {
                var next = current.next;
                current.next = prev;
                prev = current;
                current = next;
            }
           return prev;
        }
    }

    public static void test(){
        testReverse(List.of(1,2,3,4));
        testReverse(List.of(1,2,3));
        testReverse(List.of(1,2));
        testReverse(List.of(1));
    }


    private static void testReverse(Collection<Integer> num) {
        System.out.printf("%s\n",
                listNodeToString(
                        new Solution().reverseList(collectionToListNode(num))));

    }

    private static ListNode collectionToListNode(Collection<Integer> values){
        if (values.isEmpty()) {
            return null;
        }

        var head = new ListNode(values.stream().limit(1).findFirst().orElseThrow());
        var current = head;

        for (var val : values.stream().skip(1).toList()){
            current.next = new ListNode(val);
            current = current.next;
        }

        return head;
    }

    private static String listNodeToString(ListNode node) {
        var res = new StringJoiner(",", "[", "]");
        for (var cur = node; cur!=null; cur = cur.next) {
            res.add(String.valueOf(cur.val));
        }
        return res.toString();
    }
}
