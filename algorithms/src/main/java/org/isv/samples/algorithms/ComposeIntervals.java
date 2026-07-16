//
//        Задача:
//        Дан массив чисел. Необходимо сгруппировать последовательные числа в интервалы.
//        Например, если у нас есть числа [0, 1, 2, 4, 6, 7, 8], то результат должен быть "0-2,4,6-8".
//

package org.isv.samples.algorithms;
import java.util.Arrays;

public class ComposeIntervals {
    public static String composeIntervals(int [] array){
        var result = new StringBuilder();

        Arrays.sort(array);
        var left = 0;
        var right = 0;

        for(var i = 1; i < array.length; i++){
            if (array[i] -1  != array[i-1]) {
                result.append(left == right? array[left] : String.format("%d-%d",array[left],array[right]));
                result.append(",");
                left = i;
                right = left;
            } else {
                right++;
            }
        }
        if (array.length>0) {
            result.append(left == right? array[left] : String.format("%d-%d",array[left],array[right]));
        }
        return result.toString();
    }

    public static void test(){
        System.out.println(composeIntervals(new int[]{0}));
        System.out.println(composeIntervals(new int[]{0,1}));
        System.out.println(composeIntervals(new int[]{0,1,2,4}));
        System.out.println(composeIntervals(new int[]{0,2,3,4}));
        System.out.println(composeIntervals(new int[]{0,1,2,4,6,7,8}));
    }
}
