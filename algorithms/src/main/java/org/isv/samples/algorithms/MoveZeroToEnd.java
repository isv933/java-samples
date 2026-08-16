//
// Переместить все нули в конец массива in place, остальные
// элементы не должны менять порядок.
//

package org.isv.samples.algorithms;

import java.util.Arrays;

public class MoveZeroToEnd {
    public static int[]  moveZeros(int[] input){

        var writePos = 0;
        for(var readPos = 0; readPos < input.length; readPos++){

            if (input[readPos]!=0) {

                if (writePos < readPos){
                    input[writePos] = input[readPos];
                    input[readPos] = 0;
                }

                writePos++;
            }
        }

        return input;
    }

    public static void test(){
        System.out.println(Arrays
                .stream(moveZeros(new int[]{1,2})).boxed().toList());
        System.out.println(Arrays
                .stream(moveZeros(new int[]{1,0})).boxed().toList());
        System.out.println(Arrays
                .stream(moveZeros(new int[]{0,0,1,2,0,0,1})).boxed().toList());
        System.out.println(Arrays
                .stream(moveZeros(new int[]{1,2,0,3,4,5,0})).boxed().toList());
        System.out.println(Arrays
                .stream(moveZeros(new int[]{1,2,0,3,4,0,5})).boxed().toList());
    }

}
