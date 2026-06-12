package org.isv.samples.algorithms;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SortingAlgorithms {

    public static int[] burbleSort(int [] data){
        for (var i = 0; i< data.length; i++){
            for(var j =0; j < data.length - i -1; j++){
                if (data[j] > data[j+1]){
                    data [j] = data[j] ^ data[j+1];
                    data[j+1] = data[j] ^ data[j+1];
                    data[j] = data[j] ^ data[j+1];
                }
            }
        }

        return data;
    }

    public static int[] insertionSort(int[] data){
        for(var i = 1; i< data.length; i++){
            var current = data[i];
            var j = i -1;
            while (j>=0 && data[j]>current) {
                data[j+1] = data[j];
                j = j-1;
            }
            data[j+1] = current;
        }

        return data;
    }

    private static int[] selectionSort(int[] data) {
        for(var i = 0; i< data.length; i++) {

            var min_item = i;
            for (var j  = i; j < data.length; j++) {

                if (data[j] < data[min_item]) {
                    min_item = j;
                }
            }

            if (i!=min_item) {
                data[i] = data[i] ^ data[min_item];
                data[min_item] = data[i] ^ data[min_item];
                data[i] = data[i] ^ data[min_item];
            }
        }

        return data;
    }

    private static int [] mergeSort(int [] data) {
        if (data.length <= 1) {
            return data;
        }

        var middle = data.length / 2;
        var left = mergeSort(Arrays.copyOfRange(data, 0, middle));
        var right = mergeSort(Arrays.copyOfRange(data, middle, data.length));
        var leftIndex = 0;
        var rightIndex = 0;
        var resultIndex = 0;

        while(rightIndex < right.length && leftIndex < left.length) {

            if (left[leftIndex] <= right[rightIndex]) {
                data[resultIndex++] = left[leftIndex];
                leftIndex++;
            } else
            if (right[rightIndex] < left[leftIndex]) {
                data[resultIndex++] = right[rightIndex];
                rightIndex++;
            }
        }

        while (leftIndex < left.length){
            data[resultIndex++] = left[leftIndex++];
        }

        while (rightIndex < right.length){
            data[resultIndex++] = right[rightIndex++];
        }
        return data;
    }

    public static void test(){

        printResult(SortingAlgorithms.burbleSort(new int[]{2,3,4,1}));
        printResult(SortingAlgorithms.burbleSort(new int[]{1,2,3,4}));

        printResult(SortingAlgorithms.insertionSort(new int[]{2,3,4,1}));
        printResult(SortingAlgorithms.insertionSort(new int[]{1,2,3,4}));

        printResult(SortingAlgorithms.selectionSort(new int[]{2,3,4,1}));
        printResult(SortingAlgorithms.selectionSort(new int[]{1,2,3,4}));

        printResult(SortingAlgorithms.mergeSort(new int[]{2,3,4,1}));
        printResult(SortingAlgorithms.mergeSort(new int[]{1,2,3,4}));


        printResult(SortingAlgorithms.mergeSort(new int[]{1}));
        printResult(SortingAlgorithms.mergeSort(new int[]{3,2}));
        printResult(SortingAlgorithms.mergeSort(new int[]{1,2,3,4,0}));
        printResult(SortingAlgorithms.mergeSort(new int[]{1,2,3,4,0,0}));
        printResult(SortingAlgorithms.mergeSort(new int[]{1,2,3,4,4,0,0}));



    }

    private static void printResult(int[] data){
        System.out.println(Arrays.stream(data)
                .mapToObj(String::valueOf).collect(Collectors.joining(",","[","]")));
    }

}