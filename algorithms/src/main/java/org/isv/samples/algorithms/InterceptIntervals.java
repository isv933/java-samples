package org.isv.samples.algorithms;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class InterceptIntervals {

    private static List<Interval> intersect(List<Interval> user1, List<Interval> user2) {
        var result = new ArrayList<Interval>();
        var left = new IntervalIter(user1);
        var right = new IntervalIter(user2);


        while (left.hasValue() && right.hasValue()) {

            var start = Math.max(left.getValue().from, right.getValue().from);
            var end = Math.min(left.getValue().to, right.getValue().to);

            if (start < end) {
                result.add(new Interval(start, end));
            }

            if (right.getValue().to() < left.getValue().to()) {
                right.next();

            } else if (left.getValue().to() < right.getValue().to()) {
                left.next();

            } else {
                left.next();
                right.next();
            }
        }

        return result;
    }

    ;

    public static void test() {

        testIntervals(List.of(new Interval(1, 2), new Interval(3, 4)), List.of(new Interval(0, 100)));
        testIntervals(List.of(new Interval(0, 100)), List.of(new Interval(1, 2), new Interval(3, 4)));
        testIntervals(List.of(new Interval(1, 5), new Interval(6, 8),
                        new Interval(9, 15)),

                List.of(new Interval(2, 6),
                        new Interval(7, 8),
                        new Interval(10, 100)));

        testIntervals(List.of(new Interval(1, 5), new Interval(6, 8),
                        new Interval(9, 15)),

                List.of(new Interval(2, 6),
                        new Interval(7, 100)));

        testIntervals(List.of(new Interval(1, 5), new Interval(6, 8),
                        new Interval(9, 15)),

                List.of(new Interval(1, 5),
                        new Interval(7, 100)));

    }

    private static void testIntervals(List<Interval> user1, List<Interval> user2) {


        System.out.println(intersect(user1, user2).stream()
                .map(Interval::toString).collect(Collectors.joining(",", "[", "]")));

    }

    public record Interval(int from, int to) {

        public String toString() {
            return from() + ":" + to();
        }

    }

    private static class IntervalIter {
        private final Iterator<Interval> iterator;
        @Getter
        private Interval value;

        public IntervalIter(List<Interval> interval) {
            iterator = interval.iterator();
            next();
        }

        public boolean hasValue() {
            return value != null;
        }

        public void next() {
            value = !iterator.hasNext() ? null : iterator.next();
        }
    }

}
