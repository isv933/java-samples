//Написать счетчик лайков. Лайки считаются по пользователю.
//Необходимо подсчитать максимальное количество лайков по пользователю

package org.isv.samples.algorithms;

import java.util.HashMap;

public class LikesCounter {
    static class Counter {
        private final HashMap<Integer, Integer> userCount = new HashMap<>();
        private final HashMap<Integer, Integer> countUser = new HashMap<>();
        private int maxCount;

        public void like(int userId) {
            var count = userCount.compute(userId, (u, c) ->
                    c == null ? 1 : c + 1);

            maxCount = Math.max(maxCount, count);
            countUser.compute(count, (u, c) -> c == null ? 1 : c + 1);
        }

        public void unlike(int userId) {
            var currentUserCount = userCount.get(userId);
            if (currentUserCount == null) {
                return;
            }

            var countCandidate =
                    countUser.computeIfPresent(currentUserCount, (c, uc) ->
                            uc == 0 ? 0 : uc - 1);

            if (countCandidate != null && currentUserCount == maxCount && countCandidate == 0) {
                maxCount--;
            }
            userCount.put(userId, currentUserCount == 0 ? 0 : currentUserCount - 1);
        }

        public int maxCount() {
            return maxCount;

        }
    }

    public static void test() {
        var counter = new Counter();
        counter.like(1);
        System.out.printf("1 == %d\n", counter.maxCount());
        counter.like(1);
        System.out.printf("2 == %d\n", counter.maxCount());
        counter.like(2);
        System.out.printf("2 == %d\n", counter.maxCount());
        counter.unlike(1);
        System.out.printf("1 == %d\n", counter.maxCount());
        counter.like(2);
        System.out.printf("2 == %d\n", counter.maxCount());
        counter.like(3);
        counter.unlike(1);
        counter.unlike(2);
        System.out.printf("1 == %d\n", counter.maxCount());
        counter.unlike(3);
        counter.unlike(2);
        System.out.printf("0 == %d\n", counter.maxCount());
    }
}
