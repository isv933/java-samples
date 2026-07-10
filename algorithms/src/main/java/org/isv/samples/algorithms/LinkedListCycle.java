package org.isv.samples.algorithms;

import java.util.List;

public class LinkedListCycle {

    public static class ListNode {
        public ListNode(int val){
            this(val,null);
        }

        public ListNode(int val, ListNode next){
            this.val = val;
            this.next = next;
        }

        public int val;
        public ListNode next;

    }

    public static class Solution {
        public static boolean hasCycle(ListNode head) {
            if (head == null || head.next==null){
                return false;
            }

            var slow = head;
            var fast = head;

            while (fast!=null && fast.next!=null) {
                slow = slow.next;
                fast = fast.next.next;

                if (fast == slow){
                    return true;
                }
            }
            return false;
        }
    }

    static ListNode createLoopList(List<Integer> list, int loopItem) {
        ListNode head = null;
        ListNode current = null;
        ListNode loop = null;

        var index = 0;
        for(var item: list) {

            if (head == null){
                head = new ListNode(item,null);
                current = head;
            } else {
                current.next = new ListNode(item, null);
                current = current.next;
            }

            if (index++ == loopItem) {
                loop = current;
            }
        }

        if (current!=null) {
            current.next = loop;
        }

        return head;
    }



    public static void test(){
        System.out.println(Solution.hasCycle(createLoopList(List.of(0,1,2,3,4),1)));
        System.out.println(Solution.hasCycle(createLoopList(List.of(0,1,2,3,4),3)));
        System.out.println(Solution.hasCycle(createLoopList(List.of(0,1,2,3,4),10)));
    }
}
