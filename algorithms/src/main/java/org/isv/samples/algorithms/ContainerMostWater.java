//Container With Most Water (Two pointers)
//
//        Задача:
//        Дан массив высот. Найти два индекса, которые образуют контейнер с максимальной площадью.
//
//        Пример:
//
//        [1,8,6,2,5,4,8,3,7]
//        ответ: 49

package org.isv.samples.algorithms;

import static java.lang.Math.*;

public class ContainerMostWater {
    public static int getMaxArea(int[] height){
        if (height == null){
            return 0;
        }
        var p1 = 0;
        var p2 = height.length - 1;
        var maxSquare = 0;

        while (p1 < p2){
            maxSquare = max(maxSquare, min(height[p1], height[p2]) *  (p2-p1));
            if (height[p1] < height[p2]) {
                p1++;
            }  else {
                p2--;
            }
        }
        return maxSquare;
    }

    public static void test(){
        System.out.printf("%d==0\n", getMaxArea(null));
        System.out.printf("%d==0\n", getMaxArea(new int[]{0}));
        System.out.printf("%d==0\n", getMaxArea(new int[]{}));
        System.out.printf("%d==49\n", getMaxArea(new int[]{1,8,6,2,5,4,8,3,7}));
        System.out.printf("%d==1\n", getMaxArea(new int[]{1,1}));
        System.out.printf("%d==16\n", getMaxArea(new int[]{4,3,2,1,4}));
    }
}
