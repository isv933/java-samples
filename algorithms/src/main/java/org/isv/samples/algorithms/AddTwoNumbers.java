package org.isv.samples.algorithms;

import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

public class AddTwoNumbers {
    public static class ListNode {
        public  int val;
        public ListNode next;
        public ListNode() {}
        public ListNode(int val) { this.val = val; }
        public ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    };

    public static class Solution {
        public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
            var head = (ListNode) null;
            var current = (ListNode) null;
            var adder = false;

            while (l1 != null || l2 != null) {
                var total = ((l1 != null ? l1.val : 0) + (l2 != null ? l2.val : 0)) + (adder ? 1 : 0);
                var val = total % 10;
                adder = total >= 10;

                if (head == null) {
                    head = new ListNode(val, null);
                    current = head;
                } else {
                    current.next = new ListNode(val, null);
                    current = current.next;
                }

                if (l1 != null) {
                    l1 = l1.next;
                }

                if (l2 != null) {
                    l2 = l2.next;
                }
            }
            if (adder) {
                current.next = new ListNode(1,null);
            }
            return head;
        }
    }

    public static void test(){
        testAddTwoNumbers(List.of(2,4,3),List.of(5,6,4));
        testAddTwoNumbers(List.of(4,3),List.of(5,6,4));
        testAddTwoNumbers(List.of(5,6,4), List.of(4,3));
        testAddTwoNumbers(List.of(5,6,4), List.of(5,4,5));
    }

    private static void testAddTwoNumbers(Collection<Integer> num1, Collection<Integer> num2) {
        System.out.printf("%s\n",
                listNodeToString(
                        new Solution().addTwoNumbers(collectionToListNode(num1),
                                collectionToListNode(num2))));


    }

    private static String listNodeToString(ListNode node) {
        var res = new StringJoiner(",", "[", "]");
        for (var cur = node; cur!=null; cur = cur.next) {
            res.add(String.valueOf(cur.val));
        }
        return res.toString();
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
}
