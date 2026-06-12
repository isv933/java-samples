package org.isv.samples.algorithms;


public class AlgoTest {

    public static class LinkedNode<T> {
        public LinkedNode(T value, LinkedNode<T> prev, LinkedNode<T> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }

        public T value;
        public LinkedNode<T> next;
        public LinkedNode<T> prev;
    }


    public static <T> LinkedNode<T> buildTree(T[] nodes) {
        LinkedNode<T> head = null;
        var current = head;

        for (var node : nodes) {
            if (head == null) {
                head = new LinkedNode<T>(node, null, null);
                current = head;
            } else {
                current.next = new LinkedNode<>(node, current, null);
                current = current.next;
            }
        }

        return head;
    }

    public static  <T> LinkedNode<T> reverse(LinkedNode<T> head) {
        LinkedNode<T> prev = null;
        var current = head;

        while (current != null) {
            var next = current.next;
            current.next = prev;
            current.prev = next;
            prev = current;
            current = next;
        }

        return prev;
    }

    public static void test(){
            var res = reverse(buildTree(new Integer[]{1,2,3}));
            assert res!=null;


    }
}


