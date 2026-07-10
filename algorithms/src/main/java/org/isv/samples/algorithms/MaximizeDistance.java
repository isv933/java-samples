package org.isv.samples.algorithms;

public class MaximizeDistance {
        public static int maxDistToClosest(int[] seats) {
            var maxCount = 0;
            var left = -1;
            var right = -1;
            for (var i = 0; i < seats.length; i++) {
               if (seats[i] == 1) {
                   left = right;
                   right = i;
                   maxCount =  Math.max(maxCount, left == -1? right :  (right - left) /2);
                }
            }
            if (seats[seats.length-1]==0) {
                maxCount = Math.max(maxCount, seats.length-right-1);
            }
            return maxCount;
    }

    public static void test(){
        System.out.println(maxDistToClosest(new int[]{0,0,1}));
        System.out.println(maxDistToClosest(new int[]{1,0,0,1,1,0,0,0,1}));
        System.out.println(maxDistToClosest(new int[]{0,1}));
        System.out.println(maxDistToClosest(new int[]{1,0}));
        System.out.println(maxDistToClosest(new int[]{0,0,1}));
        System.out.println(maxDistToClosest(new int[]{0,0,1,0,0,0,0}));
        System.out.println(maxDistToClosest(new int[]{1,0,0,0,1,0,1}));
        System.out.println(maxDistToClosest(new int[]{0,1,1,1,0,0,1,0,0}));
        System.out.println(maxDistToClosest(new int[]{1,0,0,0,0,1}));
    }

}
