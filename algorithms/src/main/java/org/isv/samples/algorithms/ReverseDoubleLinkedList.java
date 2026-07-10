package org.isv.samples.algorithms;

import java.util.List;
import java.util.StringJoiner;

public class ReverseDoubleLinkedList {
    public static <T> LinkedList<T> reverseLinkedList(LinkedList<T> head) {
        var current = head;

        while (current != null) {
            var next = current.next;
            current.next = current.prev;
            current.prev = next;
            head = current;
            current = next;
        }

        return head;
    }

    public static void test() {
        System.out.println(reverseLinkedList(buildLinkedList(List.of(1, 2, 3))));
    }

    private static <T> LinkedList<T> buildLinkedList(List<T> list) {
        LinkedList<T> head = null;
        LinkedList<T> current = null;

        for (var l : list) {
            if (head == null) {
                head = new LinkedList<>(l, null, null);
                current = head;
            } else {
                current.next = new LinkedList<>(l, null, current);
                current = current.next;
            }
        }

        return head;
    }

    public static class LinkedList<T> {
        public T val;
        public LinkedList<T> next;
        public LinkedList<T> prev;
        public LinkedList(T val, LinkedList<T> next, LinkedList<T> prev) {
            this.val = val;
            this.next = next;
            this.prev = prev;
        }

        @Override
        public String toString() {
            var joiner = new StringJoiner(",", "[", "]");

            var current = this;
            while (current != null) {
                joiner.add(current.val.toString());
                current = current.next;

            }
            return joiner.toString();
        }
    }


}
